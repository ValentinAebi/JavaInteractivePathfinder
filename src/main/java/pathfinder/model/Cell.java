package pathfinder.model;

public final class Cell {
    private final int distanceToEnd;
    private int costFromStart = Integer.MAX_VALUE;
    private Coords parent = null;

    public Cell(int distanceToEnd) {
        this.distanceToEnd = distanceToEnd;
    }

    public boolean propose(int newCostFromStart, Coords newParent){
        boolean update = (newCostFromStart < getCostFromStart());
        if (update){
            this.costFromStart = newCostFromStart;
            this.parent = newParent;
        }
        return update;
    }

    public void setCostFromStart(int costFromStart) {
        this.costFromStart = costFromStart;
    }

    public int getDistanceToEnd() {
        return distanceToEnd;
    }

    public int getCostFromStart(){
        return costFromStart;
    }

    public int getTotalCost(){
        return getCostFromStart() + getDistanceToEnd();
    }

    public Coords getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return String.format("Cell[total cost = %d, cost from start = %d, distance to end = %d, parent = %s]",
                getTotalCost(), getCostFromStart(), getDistanceToEnd(), getParent());
    }

}
