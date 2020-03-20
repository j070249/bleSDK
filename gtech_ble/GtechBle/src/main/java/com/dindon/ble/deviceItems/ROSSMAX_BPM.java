package com.dindon.ble.deviceItems;

/**
 * Created by G-Tech on 2019/8/12.
 * E-mail: j070249@gmail.com
 */

public class ROSSMAX_BPM extends BaseDevice {

    private String dataTime = "";
    private int sys = 0;
    private int dia = 0;
    private int pulse = 0;

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
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

    public ROSSMAX_BPM(String name, String mac, String dataTime, int sys, int dia, int pulse) {
        super(name, mac);
        this.dataTime = dataTime;
        this.sys = sys;
        this.dia = dia;
        this.pulse = pulse;
    }

}
