package main;

import data.WeatherStation;
import com.fazecast.jSerialComm.*;

public class Main {
    public static void main(String[] args) {

        SerialPort p = SerialPort.getCommPort("ttvACM0");
        p.openPort();
        if (p.isOpen()) {
            while(true) {
                while (p.bytesAvailable() == 0) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                byte[] readBuffer = new byte[p.bytesAvailable()];
                int numRead = p.readBytes(readBuffer, readBuffer.length);
                String x = new String(readBuffer);
                System.out.println(x);
            }
        }
        System.out.println(p.getSystemPortName());
    }
}
