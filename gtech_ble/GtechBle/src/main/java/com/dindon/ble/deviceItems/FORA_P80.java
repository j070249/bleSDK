package com.dindon.ble.deviceItems;

/**
 * Created by G-Tech on 2019/8/12.
 * E-mail: j070249@gmail.com
 */

import android.util.Log;

import java.util.UUID;

public class FORA_P80 extends BaseDevice {
    public static final String SERVICE_UUID_STRING = "00001523-1212-efde-1523-785feabcd123";
    public static final String CHARACTEIRSTIC_UUID_STRING = "00001524-1212-efde-1523-785feabcd123";
    public static final UUID SERVICE_UUID = UUID.fromString("00001523-1212-efde-1523-785feabcd123");
    public static final UUID CHARACTEIRSTIC_UUID = UUID.fromString("00001524-1212-efde-1523-785feabcd123");

    public static final byte[] FORA_TIME_CMD = {81, 37, 0, 0, 0, 0, (byte) 163, 25};
    public static final byte[] FORA_DATA_CMD = {81, 38, 0, 0, 0, 0, (byte) 163, 26};
    public static final byte[] FORA_TURN_OFF_CMD = {81, 80, 0, 0, 0, 0, (byte) 163, 68};

    private String dataTime = "";
    private int dataType = 0;
    private int sys = 0;
    private int dia = 0;
    private int pulse = 0;

    public FORA_P80(String name, String mac, byte[] data) {
        super(name, mac);
        Log.d("BLE_LIBRARY", "FORA_P80");
        if (data.length != 8) {
            Log.d("BLE_LIBRARY", "FORA_P80 decode failed");
        } else {
            Log.d("BLE_LIBRARY", "FORA_P80 decode");
            this.dataType = 1;
            this.dataTime = String.format("%04d/%02d/%02d %02d:%02d", 0000, 00, 00, 00, 00);
            this.sys = data[3] & 0xff;
            this.dia = data[4] & 0xff;
            this.pulse = data[5] & 0xff;
        }
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getSys() {
        return sys;
    }

    public void setSys(int sys) {
        this.sys = sys;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }
}
