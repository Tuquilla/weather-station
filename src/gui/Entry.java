package gui;

import data.DataFetch;
import data.Observer;
import data.WeatherStation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Entry extends Application implements Observer {

    private final WeatherStation weatherStation = new WeatherStation();
    private final DataFetch dataFetch = new DataFetch(weatherStation);
    GridPane topLeftGridPane;
    GridPane bottomLeftGridPane;
    GridPane topRightGridPane;
    GridPane bottomRightGridPane;
    GridPane mainGridPane;
    Label topLeftLabel;
    Label bottomLeftLabel;
    Label topRightLabel;
    Label bottomRightLabel;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        final int WINDOWSSIZE_X_TOTAL = 480;
        final int WINDOWSIZE_Y_TOTAL = 320;
        weatherStation.registerObserver(this);
        Thread weatherStationFetching = new Thread(dataFetch);
        weatherStationFetching.setDaemon(true);
        weatherStationFetching.start();

        topLeftGridPane = new GridPane();
        bottomLeftGridPane = new GridPane();
        topRightGridPane = new GridPane();
        bottomRightGridPane = new GridPane();
        mainGridPane = new GridPane();

        Label topLeftTitle = new Label("Temperatur");
        topLeftTitle.setAlignment(Pos.TOP_CENTER);
        topLeftTitle.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        topLeftLabel = new Label(weatherStation.getTemperatureString() + " °C");
        topLeftLabel.setAlignment(Pos.CENTER);
        topLeftLabel.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        topLeftLabel.setPrefHeight(WINDOWSIZE_Y_TOTAL / 2.0);

        Label bottomLeftTitle = new Label("Testtitel");
        bottomLeftTitle.setAlignment(Pos.TOP_CENTER);
        bottomLeftTitle.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        bottomLeftLabel = new Label("leer");
        bottomLeftLabel.setAlignment(Pos.CENTER);
        bottomLeftLabel.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        bottomLeftLabel.setPrefHeight(WINDOWSIZE_Y_TOTAL / 2.0);

        Label topRightTitle = new Label("Luftdruck");
        topRightTitle.setAlignment(Pos.TOP_CENTER);
        topRightTitle.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        topRightLabel = new Label(weatherStation.getPressureString() + " hPa");
        topRightLabel.setAlignment(Pos.CENTER);
        topRightLabel.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        topRightLabel.setPrefHeight(WINDOWSIZE_Y_TOTAL / 2.0);

        Label bottomRightTitle = new Label("Luftfeuchtigkeit");
        bottomRightTitle.setAlignment(Pos.TOP_CENTER);
        bottomRightTitle.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        bottomRightLabel = new Label(weatherStation.getHumidityString() + "%");
        bottomRightLabel.setAlignment(Pos.CENTER);
        bottomRightLabel.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        bottomRightLabel.setPrefHeight(WINDOWSIZE_Y_TOTAL / 2.0);

        createGridPane(WINDOWSSIZE_X_TOTAL, WINDOWSIZE_Y_TOTAL, topLeftTitle, topLeftGridPane, topLeftLabel);
        topLeftGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 4px 2px 2px 4px; -fx-background-color: darkgrey;");

        createGridPane(WINDOWSSIZE_X_TOTAL, WINDOWSIZE_Y_TOTAL, bottomLeftTitle, bottomLeftGridPane, bottomLeftLabel);
        bottomLeftGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 2px 2px 4px 4px; -fx-background-color: darkgrey;");

        createGridPane(WINDOWSSIZE_X_TOTAL, WINDOWSIZE_Y_TOTAL, topRightTitle, topRightGridPane, topRightLabel);
        topRightGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 4px 4px 2px 2px; -fx-background-color: darkgrey;");

        createGridPane(WINDOWSSIZE_X_TOTAL, WINDOWSIZE_Y_TOTAL, bottomRightTitle, bottomRightGridPane, bottomRightLabel);
        bottomRightGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 2px 4px 4px 2px; -fx-background-color: darkgrey;");

        mainGridPane.add(topLeftGridPane, 0, 0);
        mainGridPane.add(bottomLeftGridPane, 0, 1);
        mainGridPane.add(topRightGridPane, 1, 0);
        mainGridPane.add(bottomRightGridPane, 1, 1);


        Scene scene = new Scene(mainGridPane, WINDOWSSIZE_X_TOTAL, WINDOWSIZE_Y_TOTAL);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    private void createGridPane(int WINDOWSSIZE_X_TOTAL, int WINDOWSIZE_Y_TOTAL, Label title, GridPane gridPane, Label label) {
        gridPane.addRow(0, title);
        gridPane.addRow(1, label);
        gridPane.setPrefWidth(WINDOWSSIZE_X_TOTAL/ 2.0);
        gridPane.setPrefHeight(WINDOWSIZE_Y_TOTAL / 2.0);
        gridPane.getChildren().get(0).setStyle("-fx-font-size: 20; -fx-border-color: green;");
        gridPane.getChildren().get(1).setStyle("-fx-font-size: 20; -fx-border-color: blue;");
    }

    @Override
    public void update() {
        Platform.runLater(() -> {
            //topLeftLabel = new Label(weatherStation.getTemperatureString() + " °C");
            topLeftLabel.setText(weatherStation.getTemperatureString() + " °C");
            topRightLabel.setText(weatherStation.getPressureString() + " hPa");
            bottomRightLabel.setText(weatherStation.getHumidityString() + "%");
            topLeftGridPane.getChildren().remove(1);
            topRightGridPane.getChildren().remove(1);
            bottomRightGridPane.getChildren().remove(1);
            topLeftGridPane.addRow(1, topLeftLabel);
            topRightGridPane.addRow(1, topRightLabel);
            bottomRightGridPane.addRow(1, bottomRightLabel);
        });
    }
}
