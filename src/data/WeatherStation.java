package data;


import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;
public class WeatherStation implements Subject {

    private SME280Dummy sme280Dummy;
    private double temperature;
    private double pressure;
    private double humidity;
    private ArrayList<Observer> observers;
    private SerialPort serialPort = null;

    public WeatherStation() {
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for (SerialPort p : serialPorts) {
            if (p.getSystemPortName().equals("COM3") || p.getSystemPortName().equals("ttyACM0")) {
                serialPort = p;
                serialPort.openPort();
            }
        }
        sme280Dummy = new SME280Dummy();
        observers = new ArrayList<>();
        getSME280Data();
    }

    public void getSME280Data() {
            if (serialPort != null && serialPort.isOpen()) {
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
                    String[] allBlocks = x.split("\n");
                    String[] data = allBlocks[allBlocks.length-1].trim().split(",");
                    temperature = Double.parseDouble(data[0]);
                    pressure = Double.parseDouble(data[1]) / 100;
                    humidity = Double.parseDouble(data[2]);
                }
                catch (Exception ignored) {

                }
            }
            else {
                double[] sensorData = sme280Dummy.getSensorData();
                temperature = sensorData[0];
                pressure = sensorData[1];
                humidity = sensorData[2];
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
