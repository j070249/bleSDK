package com.dindon.ble.deviceItems;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by G-Tech on 2019/10/22.
 * E-mail: j070249@gmail.com
 */
public class HOBO_MX1101 extends BaseDevice {

    double battery = 0;
    double humidity = 0;
    double temperature = 0;

    /**
     * @param name
     * @param mac
     * @param data
     */
    public HOBO_MX1101(String name, String mac, byte[] data) {
        super(name, mac);
        byte vers = getVersion(data);
        byte offset = (byte) (getOffset(vers) + 2);
        double rawHumidity = ((data[offset + 1] << 8) + (data[offset] & 0xFF));
        this.humidity = ( rawHumidity) == 16382.0 ? 100.0 : (( rawHumidity) / 16383.0) * 100.0;
        offset = (byte) getOffset(vers);
        int rawTemperature = ((data[offset + 1] << 8) + (data[offset] & 0xFF));
        int temp = (int)((double)rawTemperature + 0.5d);
        double tempLog10 = Math.log10((((double)temp) * 10000.0d) / (4095.0d - ((double)temp)));
        this.temperature = (tempLog10 * (((0.24814d * tempLog10) * tempLog10) * tempLog10))
                + (((560.112d - (267.344d * tempLog10)) + ((51.576d * tempLog10)
                * tempLog10)) - (((5.539d * tempLog10) * tempLog10) * tempLog10));
        offset = (byte) (getOffset(vers) + 4);
        double be = 0.01953125 * ((double) (data[offset] & 0xff));
        double BATTERY_DEAD_VOLTAGE = 1.8;
        double BATTERY_DELTA_VOLTAGE = 1.2;
        double BATTERY_FULL_VOLTAGE = 3.0;
        if (be <= BATTERY_DEAD_VOLTAGE)
            be = BATTERY_DEAD_VOLTAGE;
        if (be > BATTERY_FULL_VOLTAGE)
            be = BATTERY_FULL_VOLTAGE;
        this.battery = (((be - BATTERY_DEAD_VOLTAGE) / BATTERY_DELTA_VOLTAGE) * 100.0d);

    }

    public double getBattery() {
        return battery;
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    private long getSerial(byte[] packet) {
        long serial = packet[6] << 24 | packet[5] << 16 | packet[4] << 8 | packet[3];
        return serial;
    }

    private byte getVersion(byte[] packet) {
        byte vers = packet[2];
        return vers;
    }

    private int getModel(byte[] packet) {
        int model = packet[8] << 8 | packet[7];
        return model;
    }

    private int getOffset(int vers) {
        if (vers < 2)
            return 12;
        else if (vers < 4)
            return 13;
        else if (vers == 4)
            return 14;
        else
            return 15;
    }
}
