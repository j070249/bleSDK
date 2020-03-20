package com.dindon.ble.deviceItems;

/**
 * Created by G-Tech on 2019/8/12.
 * E-mail: j070249@gmail.com
 */

public class BaseDevice {
    private String mac;
    private String name;

    /**
     *
     * @param name
     * @param mac
     */

    public BaseDevice(String name, String mac) {
        this.mac = mac;
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public String getName() {
        return name;
    }
}
