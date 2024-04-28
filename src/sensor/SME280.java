package sensor;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

public class SME280 {

    // Werte für Temperatur Kompensation
    private static int DIG_T1; // = 27891;
    private static short DIG_T2; // = 26250;
    private static short DIG_T3; // = 50;

    // Werte für Druck Kompensation
    private static int DIG_P1; // = 36477;
    private static short DIG_P2; // = -10685;
    private static short DIG_P3; // = 3024;
    private static short DIG_P4; // = 8842;
    private static short DIG_P5; // = -13773;
    private static short DIG_P6; // = -10685;
    private static short DIG_P7; // = 65536;
    private static short DIG_P8; // = -11075;
    private static short DIG_P9; // = 3038;

    // Werte für Feuchtigkeit Kompensation
    private static int DIG_H1; // = 75;
    private static short DIG_H2; // = 356;
    private static int DIG_H3; // = 0;
    private static short DIG_H4; // = 374;
    private static short DIG_H5; // = 0;
    private static int DIG_H6; // = 30;

    private final Context pi4j;
    private final I2CProvider i2CProvider;
    private final I2CConfig i2cConfig;
    private final I2C bme280;

    // Temperatur für Druck und Feuchtigkeitsformel
    private int t_fine;

    public SME280() {
        pi4j = Pi4J.newAutoContext();
        i2CProvider = pi4j.provider("linuxfs-i2c");
        i2cConfig = I2C.newConfigBuilder(pi4j)
                .id("BME280")
                .bus(1)
                .device(0x76)
                .build();
        bme280 = i2CProvider.create(i2cConfig);
        getTempCompensationValues();
        getPressureCompensationValues();
        getHumidityCompensationValues();
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
    }

    public SME280(int value) {
        pi4j = Pi4J.newAutoContext();
        i2CProvider = pi4j.provider("linuxfs-i2c");
        i2cConfig = I2C.newConfigBuilder(pi4j)
                .id("BME280")
                .bus(1)
                .device(value)
                .build();
        bme280 = i2CProvider.create(i2cConfig);
        getTempCompensationValues();
        getPressureCompensationValues();
        getHumidityCompensationValues();
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
    }



    public double[] getSensorData() {
        double[] sensorData = new double[3];
        sensorData[0] = getTemperature();
        sensorData[1] = getPressure();
        sensorData[2]= getHumidity();
        resetSensor();
        return sensorData;
    }

    public double getTemperature() {
        int[] tempRegisterValues = readTemperatureRegisters();
        int tempRaw = getTemperatureRaw(tempRegisterValues);
        return calculateTemp(tempRaw);
    }

    private int[] readTemperatureRegisters() {
        int[] tempRegisterValues = new int[3];
        tempRegisterValues[0] = bme280.readRegister(0xFA);
        tempRegisterValues[1] = bme280.readRegister(0xFB);
        tempRegisterValues[2] = bme280.readRegister(0xFC);
        return tempRegisterValues;
    }

    private int getTemperatureRaw(int[] tempRegisterValues) {
        int TempMSB = tempRegisterValues[0];
        int TempLSB = tempRegisterValues[1];
        int TempXLSB = tempRegisterValues[2];
        return (TempMSB << 12) | (TempLSB << 4) | (TempXLSB >> 4);
    }

    private double calculateTemp(int tempRaw) {
        long tempRaw32 = ((long) tempRaw << 8) >> 8;
        int var1 = (int) (((tempRaw32 >> 3) - (DIG_T1 << 1)) * (DIG_T2) >> 11);
        int var2 = (int) (((((tempRaw32 >> 4) - (DIG_T1)) * ((tempRaw32 >> 4) - (DIG_T1))) >> 12) * (DIG_T3) >> 14);
        t_fine = var1 + var2;
        double temperature = (t_fine * 5 + 128) >> 8;
        return temperature / 100.0f;
    }

    public double getPressure() {
        int[] pressureRegisterValues = readPressureRegisters();
        int pressureRaw = getPressureRaw(pressureRegisterValues);
        return calculatePressure(pressureRaw);
    }

    private int[] readPressureRegisters() {
        int[] pressureRegisterValues = new int[3];
        pressureRegisterValues[0] = bme280.readRegister(0xF7);
        pressureRegisterValues[1] = bme280.readRegister(0xF8);
        pressureRegisterValues[2] = bme280.readRegister(0xF9);
        return pressureRegisterValues;
    }

    private int getPressureRaw(int[] pressureRegisterValues) {
        int pressMSB = pressureRegisterValues[0];
        int pressLSB = pressureRegisterValues[1];
        int pressXLSB = pressureRegisterValues[2];
        return (pressMSB << 12) | (pressLSB << 8) | (pressXLSB >> 4);
    }

    private double calculatePressure(int pressureRaw) {
        long pressureRaw32 = ((long) pressureRaw << 8) >> 8;
        double p;
        double varP1, varP2;
        varP1 = ((double)t_fine/2.0) - 64000.0;
        varP2 = varP1 * varP1 * ((double)DIG_P6) / 32768.0;
        varP2 = varP2 + varP1 * ((double)DIG_P5) * 2.0;
        varP2 = (varP2/4.0) + (((double)DIG_P4) * 65536.0);
        varP1 = (((double)DIG_P3) * varP1 * varP1 / 524288.0 + ((double)DIG_P2) * varP1) / 524288.0;
        varP1 = (1.0 + varP1 / 32768.0) * ((double)DIG_P1);

        p = 1048576.0 - (double)pressureRaw32;
        p = (p - (varP2 / 4096.0)) * 6250.0 / varP1;
        varP1 = ((double)DIG_P9) * p * p / 2147483648.0;
        varP2 = p * ((double)DIG_P8) / 32768.0;
        p = p + (varP1 + varP2 + ((double)DIG_P7)) / 16.0;
        return p / 100.000F;
    }

    public double getHumidity() {
        int[] humidityRegisterValues = readHumidityRegisters();
        int humidityRaw = getHumidityRaw(humidityRegisterValues);
        return calculateHumidity(humidityRaw);
    }

    private int[] readHumidityRegisters() {
        int[] humidityRegisterValues = new int[2];
        humidityRegisterValues[0] = bme280.readRegister(0xFD);
        humidityRegisterValues[1] = bme280.readRegister(0xFE);
        return humidityRegisterValues;
    }

    private int getHumidityRaw(int[] humidityRegisterValues) {
        int humMSB = humidityRegisterValues[0];
        int humLSB = humidityRegisterValues[1];
        return (humMSB << 8) | (humLSB);
    }

    private double calculateHumidity(int humidityRaw) {
        // Umrechnung in 32-Bit-Wert (mit MSB-Erweiterung)
        long humidityRaw32 = ((long) humidityRaw << 8) >> 8; // Hier wird das MSB (Sign Bit) wieder entfernt
        double var_H;
        var_H = (((double)t_fine) - 76800.0);
        var_H = (humidityRaw32 - (((double)DIG_H4) * 64.0 + ((double)DIG_H5) / 16384.0 * humidityRaw32)) *
                (((double)DIG_H2) / 65536.0 * (1.0 + ((double)DIG_H6) / 67108864.0 * var_H *
                        (1.0 + ((double)DIG_H3) / 67108864.0 * var_H)));
        var_H = var_H * (1.0 - ((double)DIG_H1) * var_H / 524288.0);
        if (var_H > 100.0) {
            var_H = 100.0;
        }
        else if (var_H < 0.0) {
            var_H = 0.0;
        }
        return var_H;
    }

    public void getTempCompensationValues() {
        int digT1 = bme280.readRegister(0x88);
        int digT11 = bme280.readRegister(0x89);
        int digT2 = bme280.readRegister(0x8A);
        int digT21 = bme280.readRegister(0x8B);
        int digT3 = bme280.readRegister(0x8C);
        int digT31 = bme280.readRegister(0x8D);

        DIG_T1 = (digT11 << 8) | digT1;
        DIG_T2 = (short) ((digT21 << 8) | digT2);
        DIG_T3 = (short) ((digT31 << 8) | digT3);
    }

    private void getPressureCompensationValues() {
        int digP1 = bme280.readRegister(0x8E);
        int digP11 = bme280.readRegister(0x8F);
        int digP2 = bme280.readRegister(0x90);
        int digP21 = bme280.readRegister(0x91);
        int digP3 = bme280.readRegister(0x92);
        int digP31 = bme280.readRegister(0x93);
        int digP4 = bme280.readRegister(0x94);
        int digP41 = bme280.readRegister(0x95);
        int digP5 = bme280.readRegister(0x96);
        int digP51 = bme280.readRegister(0x97);
        int digP6 = bme280.readRegister(0x98);
        int digP61 = bme280.readRegister(0x99);
        int digP7 = bme280.readRegister(0x9A);
        int digP71 = bme280.readRegister(0x9B);
        int digP8 = bme280.readRegister(0x9C);
        int digP81 = bme280.readRegister(0x9D);
        int digP9 = bme280.readRegister(0x9E);
        int digP91 = bme280.readRegister(0x9F);

        DIG_P1 = (digP11 << 8) | digP1;
        DIG_P2 = (short) ((digP21 << 8) | digP2);
        DIG_P3 = (short) ((digP31 << 8) | digP3);
        DIG_P4 = (short) ((digP41 << 8) | digP4);
        DIG_P5 = (short) ((digP51 << 8) | digP5);
        DIG_P6 = (short) ((digP61 << 8) | digP6);
        DIG_P7 = (short) ((digP71 << 8) | digP7);
        DIG_P8 = (short) ((digP81 << 8) | digP8);
        DIG_P9 = (short) ((digP91 << 8) | digP9);
    }

    private void getHumidityCompensationValues() {
        int digH1 = bme280.readRegister(0xA1);
        int digH2 = bme280.readRegister(0xE1);
        int digH21 = bme280.readRegister(0xE2);
        int digH3 = bme280.readRegister(0xE3);
        int digH4 = bme280.readRegister(0xE4);
        int digH41 = bme280.readRegister(0xE5);
        int digH5 = bme280.readRegister(0xE5);
        int digH51 = bme280.readRegister(0xE6);
        int digH6 = bme280.readRegister(0xE7);

        DIG_H1 = digH1;
        DIG_H2 = (short) ((digH21 << 8) | digH2);
        DIG_H3 = digH3;
        DIG_H4 = (short) ((digH4 << 4) | (digH41 & 0xF));
        DIG_H5 = (short) ((digH51 << 4) | (digH5 >> 4));
        DIG_H6 = digH6;
    }

    private void resetSensor() {
        int reset = 0xE0;
        int reset_cmd = 0xB6;
        int chipId = 0xD0;
        int ctrl_hum = 0xF2;
        int ctl_humSamp1 = 0x01;
        int ctrl_meas = 0xF4;
        int ctl_forced = 0x01;
        int tempOverSampleMsk = 0xE0;
        int ctl_tempSamp1 = 0x20;
        int presOverSampleMsk = 0x1C;
        int ctl_pressSamp1 = 0x04;
        int idValueMskBME = 0x60;


        bme280.writeRegister(reset, reset_cmd);
        // The sensor needs some time to complete POR steps
        try {
            Thread.sleep(300);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        int id = bme280.readRegister(chipId);
        if(id != idValueMskBME)  {
            System.out.println("Incorrect chip ID, NOT BME280");
            System.exit(42);
        }
        int ctlHum = bme280.readRegister(ctrl_hum);
        ctlHum |= ctl_humSamp1;
        byte[] humRegVal = new byte[1];
        humRegVal[0] = (byte) ctlHum;
        bme280.writeRegister(ctrl_hum, humRegVal, humRegVal.length);


        // Set forced mode to leave sleep ode state and initiate measurements.
        // At measurement completion chip returns to sleep mode
        int ctlReg = bme280.readRegister(ctrl_meas);
        ctlReg |= ctl_forced;
        ctlReg &= ~tempOverSampleMsk;   // mask off all temperature bits
        ctlReg |= ctl_tempSamp1;      // Temperature oversample 1
        ctlReg &= ~presOverSampleMsk;   // mask off all pressure bits
        ctlReg |= ctl_pressSamp1;   //  Pressure oversample 1

        byte[] regVal = new byte[1];
        regVal[0] = (byte)(ctrl_meas);
        byte[] ctlVal = new byte[1];
        ctlVal[0] = (byte) ctlReg;

        bme280.writeRegister(regVal, ctlVal, ctlVal.length);
    }
}