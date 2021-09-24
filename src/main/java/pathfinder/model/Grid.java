package pathfinder.model;

import static pathfinder.model.Util.argMustMatch;

import java.util.*;

public final class Grid {
    private final int width, height;
    private final boolean[][] traversable;

    public Grid(boolean[][] traversable, int width, int height) {
        argMustMatch(traversable.length == height, "inconsistent height");
        boolean[][] copy = new boolean[height][width];
        for (int i = 0; i < height; ++i){
            argMustMatch(traversable[i].length == width, "inconsistent width");
            copy[i] = Arrays.copyOf(traversable[i], traversable[i].length);
        }
        this.traversable = copy;
        this.width = width;
        this.height = height;
    }

    public PathSearchResult computePath(Coords startCoords, Coords endCoords){
        requireCoordinatesInBounds(startCoords);
        requireCoordinatesInBounds(endCoords);
        Cell[][] cells = new Cell[height][width];
        PriorityQueue<Coords> heap = new PriorityQueue<>(new PriorityOrdering(cells));
        List<List<List<Coords>>> adjLists = new ArrayList<>(height);
        Set<Coords> consideredCells = new HashSet<>();
        initializeDataStructures(endCoords, cells, adjLists);
        heap.add(startCoords);
        cellAtCoords(startCoords, cells).setCostFromStart(0);
        boolean found = false;
        while (!found && !heap.isEmpty()){
            Coords currCoords = heap.poll();
            consideredCells.add(currCoords);
            int costFromStartUsingCurr = cellAtCoords(currCoords, cells).getCostFromStart();
            List<Coords> adj = adjLists.get(currCoords.getLine()).get(currCoords.getCol());
            Iterator<Coords> iterator = adj.iterator();
            while (iterator.hasNext() && !found){
                Coords neibourCoords = iterator.next();
                boolean updated = cellAtCoords(neibourCoords, cells).propose(costFromStartUsingCurr + 1, currCoords);
                if (updated){
                    found = neibourCoords.equals(endCoords);
                    heap.remove(neibourCoords);
                    heap.add(neibourCoords);
                }
            }
        }
        return new PathSearchResult(found ? Optional.of(buildPath(endCoords, cells)) : Optional.empty(), consideredCells);
    }

    private List<Coords> buildPath(Coords endCoords, Cell[][] cells) {
        List<Coords> path = new LinkedList<>();
        Coords pos = endCoords;
        while (cellAtCoords(pos, cells).getParent() != null){
            path.add(pos);
            pos = cellAtCoords(pos, cells).getParent();
        }
        path.add(pos);
        Collections.reverse(path);
        return path;
    }

    private void initializeDataStructures(Coords endCoords, Cell[][] cells, List<List<List<Coords>>> adjLists) {
        for (int l = 0; l < height; ++l){
            ArrayList<List<Coords>> line = new ArrayList<>(width);
            for (int c = 0; c < width; ++c){
                int distanceToEnd = endCoords.distanceTo(l, c);
                cells[l][c] = traversable[l][c] ? new Cell(distanceToEnd) : null;
                List<Coords> adj = new ArrayList<>();
                addToAdjIfPossible(adj, l-1, c, height, width);
                addToAdjIfPossible(adj, l+1, c, height, width);
                addToAdjIfPossible(adj, l, c-1, height, width);
                addToAdjIfPossible(adj, l, c+1, height, width);
                line.add(adj);
            }
            adjLists.add(line);
        }
    }

    private void addToAdjIfPossible(List<Coords> adj, int line, int col, int height, int width){
        if (0 <= line && line < height && 0 <= col && col < width && traversable[line][col]){
            adj.add(new Coords(line, col));
        }
    }

    private final class PriorityOrdering implements java.util.Comparator<Coords> {
        private final Cell[][] cells;

        public PriorityOrdering(Cell[][] cells) {
            this.cells = cells;
        }

        @Override
        public int compare(Coords leftCoords, Coords rightCoords) {
            Cell left = cellAtCoords(leftCoords, cells), right = cellAtCoords(rightCoords, cells);
            if (left.getTotalCost() < right.getTotalCost()
                    || (left.getTotalCost() == right.getTotalCost() && left.getDistanceToEnd() < right.getDistanceToEnd())){
                return -1;
            }
            else if (left.getTotalCost() == right.getTotalCost() && left.getDistanceToEnd() == right.getDistanceToEnd()){
                return 0;
            }
            else {
                return 1;
            }
        }

    }

    private Cell cellAtCoords(Coords coords, Cell[][] cells){
        return cells[coords.getLine()][coords.getCol()];
    }

    private void requireCoordinatesInBounds(Coords coords){
        Objects.requireNonNull(coords);
        int line = coords.getLine(), col = coords.getCol();
        argMustMatch(coords.isInBounds(width, height), "coordinates out of bound");
    }

}
