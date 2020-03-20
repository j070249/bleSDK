package com.dindon.ble.deviceItems;

import android.util.Log;

import java.util.UUID;

/**
 * Created by G-Tech on 2019/10/16.
 * E-mail: j070249@gmail.com
 */
public class FORA_W310 extends BaseDevice {
    public static final String SERVICE_UUID_STRING = "00001523-1212-efde-1523-785feabcd123";
    public static final String CHARACTEIRSTIC_UUID_STRING = "00001524-1212-efde-1523-785feabcd123";
    public static final UUID SERVICE_UUID = UUID.fromString("00001523-1212-efde-1523-785feabcd123");
    public static final UUID CHARACTEIRSTIC_UUID = UUID.fromString("00001524-1212-efde-1523-785feabcd123");

    public static final byte[] FORA_DATA_CMD = {81, 113, 2, 1, 0, (byte) 163, 104};
    public static final byte[] FORA_TURN_OFF_CMD = {81, 80, 0, 0, 0, 0, (byte) 163, 68};

    private String dataTime = "";
    private int height = 0;
    private double weight = 0;
    private int age = 0;
    private double bodyFat = 0;
    private int bmr = 0;
    private double bmi = 0;


    /**
     * @param name
     * @param mac
     * @param data
     */
    public FORA_W310(String name, String mac, byte[] data) {
        super(name, mac);
        Log.d("BLE_LIBRARY", "FORA_W310");
        if (data.length != 34 || data[1] != 0x71) {
            Log.d("BLE_LIBRARY", "FORA_W310 decode failed");
        } else {
            Log.d("BLE_LIBRARY", "FORA_W310 decode");
            int year = (data[4] & 0xff) + 2000;
            int month = (data[5] & 0xff);
            int day = (data[6] & 0xff);
            int hour = (data[7] & 0xff);
            int minute = (data[8] & 0xff);
            this.dataTime = String.format("%04d/%02d/%02d %02d:%02d", year, month, day, hour, minute);
            this.height = (data[11] & 0xff);
            this.weight = Double.valueOf(((data[16] & 0xff) << 8) + (data[17]& 0xff)) / 10;
            this.age = (data[14] & 0xff);
            this.bodyFat = Double.valueOf(((data[20] & 0xff) << 8) + (data[21]& 0xff)) / 10;
            this.bmr = (data[22] << 8) + data[23];
            this.bmi = Double.valueOf(((data[24] & 0xff) << 8) + (data[25]& 0xff)) / 10;
        }
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(double bodyFat) {
        this.bodyFat = bodyFat;
    }

    public int getBmr() {
        return bmr;
    }

    public void setBmr(int bmr) {
        this.bmr = bmr;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
}
