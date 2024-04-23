package main;

import data.WeatherStation;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hallo Welt");

        WeatherStation weatherStation = new WeatherStation();
        System.out.println("Temperature: " + weatherStation.getTemperature());
        System.out.println("Luftdruck: " + weatherStation.getPressure());
        System.out.println("Luftfeuchtigkeit: " + weatherStation.getHumidity());
        weatherStation.getSME280Data();
        System.out.println("\nTemperature: " + weatherStation.getTemperature());
        System.out.println("Luftdruck: " + weatherStation.getPressure());
        System.out.println("Luftfeuchtigkeit: " + weatherStation.getHumidity());
    }
}