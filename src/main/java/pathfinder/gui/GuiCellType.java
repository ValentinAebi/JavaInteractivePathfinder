package pathfinder.gui;

import javafx.scene.paint.Color;

public enum GuiCellType {
    NORMAL(Color.WHITE), OBSTACLE(Color.BLACK), START(Color.BLUE), END(Color.GREEN), IN_PATH(Color.RED), CONSIDERED(Color.YELLOW);

    private final Color color;

    GuiCellType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
