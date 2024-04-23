package data;

//import sensor.SME280;

import java.util.ArrayList;
public class WeatherStation implements Subject {

    private final SME280Dummy sme280;
    private double temperature;
    private double pressure;
    private double humidity;
    private ArrayList<Observer> observers;

    public WeatherStation() {
        sme280 = new SME280Dummy();
        observers = new ArrayList<>();
        getSME280Data();
    }

    public void getSME280Data() {
        double[] data = sme280.getSensorData();
        temperature = data[0];
        pressure = data[1];
        humidity = data[2];
        notifyObserver();
    }

    public double getTemperature() {
        return temperature;
    }

    public String getTemperatureString() {
        return Double.toString(Math.round(getTemperature() * 100) / 100.0);
    }

    public double getPressure() {
        return pressure;
    }

    public String getPressureString() {
        return Double.toString(Math.round(getPressure() * 100) / 100.0);
    }

    public double getHumidity() {
        return humidity;
    }

    public String getHumidityString() {
        return Double.toString(Math.round(getHumidity() * 100) / 100.0);
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver() {
        for (Observer observer: observers) {
            System.out.println("ich habe informiert");
            observer.update();
        }
    }
}
