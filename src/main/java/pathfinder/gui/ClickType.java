package pathfinder.gui;

import pathfinder.model.Coords;

import java.util.function.BiConsumer;

import static pathfinder.gui.GuiCellType.*;

public enum ClickType {
    INVERT_OBSTACLE("Obstacles", (grid, cell) -> {
        cell.setCelltype(cell.getCelltype().equals(OBSTACLE) ? NORMAL : OBSTACLE);
    }),
    SET_START("Start", (grid, cell) -> {
        Coords prevStart = grid.getStart();
        if (prevStart != null){
            grid.cellAt(prevStart).setCelltype(NORMAL);
        }
        cell.setCelltype(START);
        grid.setStart(cell.getCoords());
    }),
    SET_END("End", (grid, cell) -> {
        Coords prevEnd = grid.getEnd();
        if (prevEnd != null){
            grid.cellAt(prevEnd).setCelltype(NORMAL);
        }
        cell.setCelltype(END);
        grid.setEnd(cell.getCoords());
    });

    private final String descr;
    private final BiConsumer<GuiGrid, GuiCell> action;

    ClickType(String descr, BiConsumer<GuiGrid, GuiCell> action) {
        this.descr = descr;
        this.action = action;
    }

    public void runAction(GuiGrid grid, GuiCell cell){
        grid.removeAllSpecialRoles(cell);
        action.accept(grid, cell);
    }

    @Override
    public String toString(){
        return descr;
    }

}
