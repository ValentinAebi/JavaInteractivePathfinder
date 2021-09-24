package pathfinder.model;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class PathSearchResult {
    private final Optional<List<Coords>> pathOpt;
    private final Set<Coords> consideredCells;

    public PathSearchResult(Optional<List<Coords>> pathOpt, Set<Coords> consideredCells) {
        this.pathOpt = pathOpt.map(List::copyOf);
        this.consideredCells = Set.copyOf(consideredCells);
    }

    public Optional<List<Coords>> getPathOpt() {
        return pathOpt;
    }

    public Set<Coords> getConsideredCells() {
        return consideredCells;
    }

}
