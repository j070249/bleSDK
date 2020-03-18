package com.dindon.ble.deviceItems;

/**
 * Created by G-Tech on 2019/9/10.
 * E-mail: j070249@gmail.com
 */
public class WC extends BaseDevice {

    int event = -1;

    /**
     * @param name
     * @param mac
     * @param data
     */
    public WC(String name, String mac, byte[] data) {
        super(name, mac);
        this.event = data[0];
    }
    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
