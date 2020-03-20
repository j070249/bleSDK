package com.dindon.ble.deviceItems;

import android.util.Log;

/**
 * Created by G-Tech on 2019/10/22.
 * E-mail: j070249@gmail.com
 */
public class MITEMP extends BaseDevice {

    int battery = 0;
    double humidity = 0;
    double temperature = 0;

    /**
     * @param name
     * @param mac
     * @param data
     */
    public MITEMP(String name, String mac, byte[] dataBattery, byte[] data) {
        super(name, mac);
        if (dataBattery.length == 18) {
            this.battery = dataBattery[16] & 0xff;
        }
        if (data.length == 21) {
            this.temperature = (data[17] << 8) + (data[16] & 0xFF);
            this.humidity = (data[19] << 8) + (data[18] & 0xFF);
        }
        Log.d("MITEMP","battery :" + battery + "/data :" + temperature + ";" + humidity);
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
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
}
