package pathfinder.model;

import java.util.Objects;

public final class Coords {
    private final int line, col;

    public Coords(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public int distanceTo(int l, int c){
        return Math.abs(this.line-l) + Math.abs(this.col-c);
    }

    public boolean isInBounds(int width, int height){
        return 0 <= col && col < width && 0 <= line && line < height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coords coords = (Coords) o;
        return getLine() == coords.getLine() && getCol() == coords.getCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLine(), getCol());
    }

    @Override
    public String toString() {
        return String.format("(line = %d ; col = %d)", getLine(), getCol());
    }

}
