package com.dindon.ble.deviceItems;

/**
 * Created by G-Tech on 2019/8/12.
 * E-mail: j070249@gmail.com
 */

public class ROSSMAX_BGM extends BaseDevice {

    private String dataTime = "";
    private int gluecose = 0;

    public ROSSMAX_BGM(String name, String mac, String dataTime, int gluecose) {
        super(name, mac);
        this.dataTime = dataTime;
        this.gluecose = gluecose;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public int getGluecose() {
        return gluecose;
    }

    public void setGluecose(int gluecose) {
        this.gluecose = gluecose;
    }

}
