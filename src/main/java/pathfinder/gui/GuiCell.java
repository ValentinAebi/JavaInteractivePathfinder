package pathfinder.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pathfinder.model.Coords;

public final class GuiCell extends Rectangle {
    private final Coords coords;
    private final ObjectProperty<GuiCellType> celltype = new SimpleObjectProperty<>(GuiCellType.NORMAL);

    public GuiCell(double width, double height, Coords coords) {
        super(width, height);
        this.coords = coords;
        fillProperty().bind(Bindings.createObjectBinding(() -> getCelltype().getColor(), celltypeProperty()));
        strokeProperty().bind(fillProperty());
    }

    public GuiCellType getCelltype() {
        return celltype.get();
    }

    public ObjectProperty<GuiCellType> celltypeProperty() {
        return celltype;
    }

    public void setCelltype(GuiCellType celltype) {
        this.celltype.set(celltype);
    }

    public Coords getCoords() {
        return coords;
    }

}
