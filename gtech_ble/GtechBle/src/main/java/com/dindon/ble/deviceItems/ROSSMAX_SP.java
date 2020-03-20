package com.dindon.ble.deviceItems;

/**
 * Created by G-Tech on 2019/8/12.
 * E-mail: j070249@gmail.com
 */

public class ROSSMAX_SP extends BaseDevice {

    private String dataTime = "";
    private int pulse = 0;
    private int sp02 = 0;

    public ROSSMAX_SP(String name, String mac, String dataTime, int pulse, int sp02) {
        super(name, mac);
        this.dataTime = dataTime;
        this.pulse = pulse;
        this.sp02 = sp02;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getSp02() {
        return sp02;
    }

    public void setSp02(int sp02) {
        this.sp02 = sp02;
    }
}
