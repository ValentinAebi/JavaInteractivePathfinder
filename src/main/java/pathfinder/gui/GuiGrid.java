package pathfinder.gui;

import javafx.scene.control.Alert;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pathfinder.model.*;

import java.util.Objects;
import java.util.function.Consumer;

import static pathfinder.gui.ClickType.*;
import static pathfinder.gui.GuiCellType.*;

public final class GuiGrid extends BorderPane {

    private static final double OBSTACLE_PROBA = 0.37;

    private final int nbLines, nbColumns;
    private final GuiCell[][] cells;
    private Coords start = null, end = null;
    private ClickType clickType = INVERT_OBSTACLE;

    public GuiGrid(int nbLines, int nbColumns, double cellSize) {
        cells = new GuiCell[nbLines][nbColumns];
        this.nbLines = nbLines;
        this.nbColumns = nbColumns;
        VBox rootPane = new VBox();
        for (int line = 0; line < nbLines; ++line){
            HBox lineHBox = new HBox();
            for (int col = 0; col < nbColumns; ++col){
                GuiCell cell = new GuiCell(cellSize, cellSize, new Coords(line, col));
                cell.addEventFilter(MouseDragEvent.DRAG_DETECTED, mouseEvent -> {
                    getClickType().runAction(this, cell);
                    startFullDrag();
                });
                cell.setOnMouseDragEntered(mouseEvent -> getClickType().runAction(this, cell));
                cell.setOnMouseClicked(mouseEvent -> getClickType().runAction(this, cell));
                cells[line][col] = cell;
                lineHBox.getChildren().add(cell);
            }
            rootPane.getChildren().add(lineHBox);
        }
        setCenter(rootPane);
        setStyle("-fx-background-color: lightgray");
    }

    public void computeAndDrawPath(){
        clearPath();
        if (start == null){
            showNotification("Cannot compute path", "No start selected");
        }
        else if (end == null){
            showNotification("Cannot compute path", "No end selected");
        }
        else {
            boolean[][] traversable = new boolean[nbLines][nbColumns];
            for (int l = 0; l < nbLines; ++l){
                for (int c = 0; c < nbColumns; ++c){
                    traversable[l][c] = !cells[l][c].getCelltype().equals(OBSTACLE);
                }
            }
            Grid grid = new Grid(traversable, nbColumns, nbLines);
            PathSearchResult pathRes = grid.computePath(start, end);
            for (Coords coords : pathRes.getConsideredCells()) {
                markCellIfNeeded(coords, CONSIDERED);
            }
            if (pathRes.getPathOpt().isEmpty()){
                showNotification("No path found", "No path found");
            }
            else {
                for (Coords coords : pathRes.getPathOpt().get()) {
                    markCellIfNeeded(coords, GuiCellType.IN_PATH);
                }
            }
        }
    }

    public void clearPath(){
        foreachCell(cell -> {
            GuiCellType cellType = cell.getCelltype();
            if (cellType.equals(IN_PATH) || cellType.equals(CONSIDERED)){
                cell.setCelltype(NORMAL);
            }
        });
    }

    public void clearObstacles(){
        clearPath();
        foreachCell(cell -> {
            GuiCellType cellType = cell.getCelltype();
            if (cellType.equals(OBSTACLE)){
                cell.setCelltype(NORMAL);
            }
        });
    }

    public void setRandomObstacles(){
        clearObstacles();
        foreachCell(cell -> {
            GuiCellType celltype = cell.getCelltype();
            if (!(celltype.equals(START) || celltype.equals(END))){
                double r = Math.random();
                if (r >= 1-OBSTACLE_PROBA){
                    cell.setCelltype(OBSTACLE);
                }
            }
        });
    }

    public GuiCell cellAt(Coords coords){
        Objects.requireNonNull(coords);
        Util.argMustMatch(coords.isInBounds(nbColumns, nbLines), "coordinates out of bound");
        return cells[coords.getLine()][coords.getCol()];
    }

    public void removeAllSpecialRoles(GuiCell cell){
        Coords coords = cell.getCoords();
        Util.argMustMatch(cellAt(coords).equals(cell), "coords assigned to cell do not match grid");
        if (start != null && start.equals(coords)){
            start = null;
        }
        if (end != null && end.equals(coords)){
            end = null;
        }
    }

    private void foreachCell(Consumer<GuiCell> action){
        for (GuiCell[] line : cells) {
            for (GuiCell cell : line) {
                action.accept(cell);
            }
        }
    }

    private void markCellIfNeeded(Coords coords, GuiCellType cellType) {
        GuiCell cell = cellAt(coords);
        GuiCellType prevCellType = cell.getCelltype();
        if (!(prevCellType.equals(GuiCellType.START) || prevCellType.equals(GuiCellType.END))){
            cell.setCelltype(cellType);
        }
    }

    public Coords getStart() {
        return start;
    }

    public void setStart(Coords start) {
        this.start = start;
    }

    public Coords getEnd() {
        return end;
    }

    public void setEnd(Coords end) {
        this.end = end;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public void setClickType(ClickType clickType) {
        this.clickType = clickType;
    }

    private void showNotification(String title, String text){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }

}
