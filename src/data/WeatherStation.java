package data;


import com.fazecast.jSerialComm.SerialPort;
import sensor.SME280;

import java.util.ArrayList;
public class WeatherStation implements Subject {

    private SME280 sme280;
    private double temperature;
    private double pressure;
    private double humidity;
    private ArrayList<Observer> observers;
    private final String COMPORT = "COM3";
    private SerialPort serialPort;

    public WeatherStation() {
        serialPort = SerialPort.getCommPort(COMPORT);
        serialPort.openPort();
        if (serialPort.isOpen());
        else {
            sme280 = new SME280();
        }
        observers = new ArrayList<>();
        getSME280Data();
    }

    public void getSME280Data() {
        if (serialPort.isOpen()) {
                while (serialPort.bytesAvailable() == 0) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                serialPort.readBytes(readBuffer, readBuffer.length);
                String x = new String(readBuffer);
                try {
                    String[] data = x.split(",");
                    temperature = Double.parseDouble(data[0]);
                    pressure = Double.parseDouble(data[1]) / 100;
                    humidity = Double.parseDouble(data[2]);
                }
                catch (Exception ignored) {

                }
        }
        else {
            double[] data = sme280.getSensorData();
            temperature = data[0];
            pressure = data[1];
            humidity = data[2];
        }
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
            observer.update();
        }
    }
}
