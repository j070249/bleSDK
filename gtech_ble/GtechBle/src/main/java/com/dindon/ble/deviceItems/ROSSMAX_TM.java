package com.dindon.ble.deviceItems;

/**
 * Created by G-Tech on 2019/8/12.
 * E-mail: j070249@gmail.com
 */

import android.util.Log;

import com.simplive.libblemoduleparser.tools.Definition;

import java.util.UUID;

public class ROSSMAX_TM extends BaseDevice {

    private String dataTime = "";
    private double temperature = 0;

    public ROSSMAX_TM(String name, String mac, double temperature, String dataTime) {
        super(name, mac);
        this.dataTime = dataTime;
        this.temperature = temperature;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        temperature = temperature;
    }
}
