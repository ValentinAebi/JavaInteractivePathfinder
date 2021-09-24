package pathfinder.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        GuiGrid grid = new GuiGrid(125, 265, 4);
        BorderPane rootPane = new BorderPane(grid);
        rootPane.setTop(createCommandBar(grid));
        primaryStage.setWidth(1500);
        primaryStage.setHeight(770);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(rootPane));
        primaryStage.show();
    }

    private HBox createCommandBar(GuiGrid grid){
        ChoiceBox<ClickType> clickTypeChooser = new ChoiceBox<>();
        clickTypeChooser.setItems(FXCollections.observableArrayList(ClickType.values()));
        clickTypeChooser.getSelectionModel().select(ClickType.INVERT_OBSTACLE);
        clickTypeChooser.setOnAction(actionEvent -> grid.setClickType(clickTypeChooser.getSelectionModel().getSelectedItem()));
        Button computePathButton = new Button("Compute path");
        computePathButton.setOnAction(actionEvent -> grid.computeAndDrawPath());
        Button clearPathButton = new Button("Clear path");
        clearPathButton.setOnAction(actionEvent -> grid.clearPath());
        Button clearObstaclesButton = new Button("Clear obstacles");
        clearObstaclesButton.setOnAction(actionEvent -> grid.clearObstacles());
        Button randomButton = new Button("Random obstacles");
        randomButton.setOnAction(actionEvent -> grid.setRandomObstacles());
        return new HBox(clickTypeChooser, computePathButton, clearPathButton, clearObstaclesButton, randomButton);
    }

}