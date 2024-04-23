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
        topLeftLabel = new Label(weatherStation.getTemperatureString() + " °C");
        topLeftLabel.setAlignment(Pos.CENTER);

        Label bottomLeftTitle = new Label("Testtitel");
        bottomLeftTitle.setAlignment(Pos.TOP_CENTER);
        bottomLeftLabel = new Label("leer");
        bottomLeftLabel.setAlignment(Pos.CENTER);

        Label topRightTitle = new Label("Luftdruck");
        topRightTitle.setAlignment(Pos.TOP_CENTER);
        topRightLabel = new Label(weatherStation.getPressureString() + " hPa");
        topRightLabel.setAlignment(Pos.CENTER);

        Label bottomRightTitle = new Label("Luftfeuchtigkeit");
        bottomRightTitle.setAlignment(Pos.TOP_CENTER);
        bottomRightLabel = new Label(weatherStation.getHumidityString() + "%");
        bottomRightLabel.setAlignment(Pos.CENTER);

        topLeftGridPane.addRow(0, topLeftTitle);
        topLeftGridPane.addRow(1, topLeftLabel);
        topLeftGridPane.setPrefHeight(WINDOWSIZE_Y_TOTAL);
        topLeftGridPane.setPrefWidth(WINDOWSSIZE_X_TOTAL/ 2.0);
        topLeftGridPane.getChildren().get(0).setStyle("-fx-font-size: 20;");
        topLeftGridPane.getChildren().get(1).setStyle("-fx-font-size: 20;");
        topLeftGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 4px 2px 2px 4px; -fx-background-color: darkgrey;");

        bottomLeftGridPane.addRow(0, bottomLeftTitle);
        bottomLeftGridPane.addRow(1, bottomLeftLabel);
        bottomLeftGridPane.setPrefHeight(WINDOWSIZE_Y_TOTAL);
        bottomLeftGridPane.setPrefWidth(WINDOWSSIZE_X_TOTAL/ 2.0);
        bottomLeftGridPane.setAlignment(Pos.CENTER);
        bottomLeftGridPane.getChildren().get(0).setStyle("-fx-font-size: 20;");
        bottomLeftGridPane.getChildren().get(1).setStyle("-fx-font-size: 20;");
        bottomLeftGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 2px 2px 4px 4px; -fx-background-color: darkgrey;");

        topRightGridPane.addRow(0, topRightTitle);
        topRightGridPane.addRow(1, topRightLabel);
        topRightGridPane.setPrefHeight(WINDOWSIZE_Y_TOTAL/ 2.0);
        topRightGridPane.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        topRightGridPane.setAlignment(Pos.CENTER);
        //topRightGridPane.getChildren().get(0).setStyle("-fx-font-size: 20;");
        //topRightGridPane.getChildren().get(1).setStyle("-fx-font-size: 20;");
        topRightGridPane.setStyle("-fx-border-color: grey; -fx-border-width: 4px 4px 2px 2px; -fx-background-color: darkgrey;");

        bottomRightGridPane.addRow(0, bottomRightTitle);
        bottomRightGridPane.addRow(1, bottomRightLabel);
        bottomRightGridPane.setPrefHeight(WINDOWSIZE_Y_TOTAL);
        bottomRightGridPane.setPrefWidth(WINDOWSSIZE_X_TOTAL / 2.0);
        bottomRightGridPane.setAlignment(Pos.CENTER);
        bottomRightGridPane.getChildren().get(0).setStyle("-fx-font-size: 20;");
        bottomRightGridPane.getChildren().get(1).setStyle("-fx-font-size: 20;");
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
