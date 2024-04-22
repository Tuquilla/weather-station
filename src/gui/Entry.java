package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Entry extends Application {

    private final Pane root = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        final int WINDOWSSIZE_X_TOTAL = 480;
        final int WINDOWSIZE_Y_TOTAL = 320;

        GridPane topLeftGridPane = new GridPane();
        GridPane bottomLeftGridPane = new GridPane();
        GridPane topRightGridPane = new GridPane();
        GridPane bottomRightGridPane = new GridPane();
        GridPane mainGridpane = new GridPane();

        Label topLeftLabel = new Label("links oben");
        Label bottomLeftLabel = new Label("links unten");
        Label rightTopLabel = new Label("rechts oben");
        Label rightBottomLabel = new Label("rechts unten");

        topLeftGridPane.add(topLeftLabel, 0, 0);
        topLeftGridPane.setPrefHeight(WINDOWSIZE_Y_TOTAL);
        topLeftGridPane.setPrefWidth(WINDOWSSIZE_X_TOTAL/ 2.0);
        topLeftGridPane.setAlignment(Pos.CENTER);
        topLeftGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 4px 2px 2px 4px; -fx-background-color: darkgrey;");

        bottomLeftGridPane.add(bottomLeftLabel, 0, 0);
        bottomLeftGridPane.setPrefHeight(WINDOWSIZE_Y_TOTAL);
        bottomLeftGridPane.setPrefWidth(WINDOWSSIZE_X_TOTAL/ 2.0);
        bottomLeftGridPane.setAlignment(Pos.CENTER);
        bottomLeftGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 2px 2px 4px 4px; -fx-background-color: darkgrey;");

        topRightGridPane.add(rightTopLabel, 1, 0);
        topRightGridPane.setPrefHeight(WINDOWSIZE_Y_TOTAL/ 2.0);
        topRightGridPane.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        topRightGridPane.setAlignment(Pos.CENTER);
        topRightGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 4px 4px 2px 2px; -fx-background-color: darkgrey;");

        bottomRightGridPane.add(rightBottomLabel, 1, 1);
        bottomRightGridPane.setPrefHeight(WINDOWSIZE_Y_TOTAL);
        bottomRightGridPane.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        bottomRightGridPane.setAlignment(Pos.CENTER);
        bottomRightGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 2px 4px 4px 2px; -fx-background-color: darkgrey;");

        mainGridpane.add(topLeftGridPane, 0, 0);
        mainGridpane.add(bottomLeftGridPane, 0, 1);
        mainGridpane.add(topRightGridPane, 1, 0);
        mainGridpane.add(bottomRightGridPane, 1, 1);


        Scene scene = new Scene(mainGridpane, WINDOWSSIZE_X_TOTAL, WINDOWSIZE_Y_TOTAL);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}
