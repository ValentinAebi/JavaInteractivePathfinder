module pathfinder.pathfinder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens pathfinder.gui to javafx.fxml;
    exports pathfinder.gui;
}