package com.dindon.ble;

/**
 * Created by G-Tech on 2019/8/12.
 * E-mail: j070249@gmail.com
 */

public class GtechException{

    public static final Integer SUCCESS = 0x01;
    public static final Integer CONNECT_DEVICE_FAILD = 0x02;
    public static final Integer DISCONNECT_FROM_DEVICE = 0x03;

    public static final Integer REQUEST_BLE_OPEN = 0x04;
    public static final Integer REQUEST_GPS_OPEN = 0x05;
    public static final Integer REQUEST_ACCESS_FINE_LOCATION_PERMISSION = 0x06;

    public static final Integer ON_SCAN_FINISHED = 0x07;
    public static final Integer ON_SCAN_STARTED = 0x08;



    private int code;
    private String description;

    public GtechException(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public GtechException setCode(int code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GtechException setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "GtechException { " +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
