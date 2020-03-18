package com.dindon.ble.deviceItems;

import android.util.Log;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by G-Tech on 2019/9/11.
 * E-mail: j070249@gmail.com
 */
public class FORA_TD8255 extends BaseDevice {
    public static final String SERVICE_UUID_STRING = "00001523-1212-efde-1523-785feabcd123";
    public static final String CHARACTEIRSTIC_UUID_STRING = "00001524-1212-efde-1523-785feabcd123";
    public static final UUID SERVICE_UUID = UUID.fromString("00001523-1212-efde-1523-785feabcd123");
    public static final UUID CHARACTEIRSTIC_UUID = UUID.fromString("00001524-1212-efde-1523-785feabcd123");

    public static final byte[] FORA_TIME_CMD = {81, 37, 0, 0, 0, 0, (byte) 163, 25};
    public static final byte[] FORA_DATA_CMD = {81, 38, 0, 0, 0, 0, (byte) 163, 26};
    public static final byte[] FORA_TURN_OFF_CMD = {81, 80, 0, 0, 0, 0, (byte) 163, 68};

    private String dataTime = "";
    private int dataType = 0;
    private int SpO2 = 0;
    private int pulse = 0;

    /**
     * @param name
     * @param mac
     * @param data
     * @param time
     */
    public FORA_TD8255(String name, String mac, byte[] data, byte[] time) {
        super(name, mac);
        Log.d("BLE_LIBRARY", "FORA_TD8255");
        if (data.length != 8 || time.length != 8 || time[1] != 0x25) {
            Log.d("BLE_LIBRARY", "FORA_TD8255 decode failed");
        } else {
            Log.d("BLE_LIBRARY", "FORA_TD8255 decode");
            this.dataType = (time[4] & 0xff) >> 7 == 1 ? 1 : 2;
            int year = ((time[3] & 0xff) >> 1) + 2000;
            int month = ((time[2] & 0xff) >> 5) + ((time[3] & 0x01) << 3);
            int day = time[2] & 0x1f;
            int hour = time[5] & 0x1f;
            int minute = time[4] & 0x3f;
            this.dataTime = String.format("%04d/%02d/%02d %02d:%02d", year, month, day, hour, minute);
            this.SpO2 = data[3] << 8 | data[2];
            this.pulse = data[5] & 0xff;
        }
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public int getSpO2() {
        return SpO2;
    }

    public void setSpO2(int spO2) {
        SpO2 = spO2;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }
}
