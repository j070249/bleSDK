package com.dindon.ble.deviceItems;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.clj.fastble.data.BleDevice;

/**
 * Created by G-Tech on 2019/9/10.
 * E-mail: j070249@gmail.com
 */
public class WP extends BaseDevice {

    byte[] sensor = new byte[4];
    int event = -1;
    int battery = -1;

    /**
     * @param name
     * @param mac
     * @param data
     */
    public WP(String name, String mac, byte[] data) {
        super(name, mac);
        System.arraycopy(data, 1, this.sensor, 0, 4);
        this.event = data[5];
        this.battery = data[6];
    }

    public byte[] getSensor() {
        return sensor;
    }

    public void setSensor(byte[] sensor) {
        this.sensor = sensor;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }
}
