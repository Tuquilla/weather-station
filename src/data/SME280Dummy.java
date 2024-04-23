package data;

public class SME280Dummy {

    final double TEMP_MIN = 18.0;
    final double TEMP_MAX = 24.0;
    final double PRESSURE_MIN = 950.0;
    final double PRESSURE_MAX = 1025.0;
    final double HUMIDITY_MIN = 60.0;
    final double HUMIDITY_MAX = 100.0;

    public double[] getSensorData() {
        double[] sensorData = new double[3];
        sensorData[0] = TEMP_MIN + Math.random() * (TEMP_MAX - TEMP_MIN);
        sensorData[1] = PRESSURE_MIN + Math.random() * (PRESSURE_MAX - PRESSURE_MIN);
        sensorData[2] = HUMIDITY_MIN + Math.random() * (HUMIDITY_MAX - HUMIDITY_MIN);
        return sensorData;
    }
}
