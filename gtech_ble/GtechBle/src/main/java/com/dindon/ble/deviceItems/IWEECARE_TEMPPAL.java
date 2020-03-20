package com.dindon.ble.deviceItems;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by G-Tech on 2019/10/22.
 * E-mail: j070249@gmail.com
 */
public class IWEECARE_TEMPPAL extends BaseDevice {

    double battery = 0;
    double temperature = 0;

    /**
     * @param name
     * @param mac
     * @param data
     */
    public IWEECARE_TEMPPAL(String name, String mac, byte[] data) {
        super(name, mac);
        Log.d("IWEECARE_TEMPPAL", "data :" + Arrays.toString(data));
        int battery = (data[20] << 8) + (data[21] & 0xFF);
        int temp = (data[22] << 8) + (data[23] & 0xFF);
        Log.d("IWEECARE_TEMPPAL", "battery :" + battery);
        Log.d("IWEECARE_TEMPPAL", "temp :" + temp);

    }

    public double getBattery() {
        return battery;
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
