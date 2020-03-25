package com.dindon.ble.deviceBleUtils;

import android.bluetooth.BluetoothGatt;
import android.os.Handler;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.dindon.ble.GtechBle;
import com.dindon.ble.deviceItems.FORA_D40;
import com.dindon.ble.deviceItems.FORA_P80;

import java.util.Arrays;

/**
 * Created by G-Tech on 2019/8/13.
 * E-mail: j070249@gmail.com
 */
public class FORA_P80Utils {

    private static final String TAG = FORA_P80Utils.class.getSimpleName();

    Handler handler = new Handler();
    private int cmd_state = 0;
    private int timeOut = 0;
    private byte[] foraData, foraTime;
    private BleDevice bleDevice;
    private GtechBle.dataResultCallback dataResultCallback;

    public FORA_P80Utils(BleDevice _bleDevice, BluetoothGatt gatt, GtechBle.dataResultCallback _dataResultCallback) {
        this.bleDevice = _bleDevice;
        this.dataResultCallback = _dataResultCallback;
        BleManager.getInstance().notify(
                bleDevice,
                FORA_P80.SERVICE_UUID_STRING,
                FORA_P80.CHARACTEIRSTIC_UUID_STRING, new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {

                    }

                    @Override
                    public void onNotifyFailure(BleException exception) {
                        Log.d(TAG, exception.toString());
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {

//                        Log.d(TAG, String.valueOf(data.length));
                        if (data.length != 8) {
                            return;
                        }

                        int[] ints = new int[8];
                        for (int i = 0; i < 8; i++) {
                            ints[i] = data[i] & 0xff;
                        }
                        Log.d(TAG, Arrays.toString(ints));

                        if (data[1] == 67) {
                            dataResultCallback.onSuccess(new FORA_P80(bleDevice.getName(), bleDevice.getMac(), data));
                            handler.postDelayed(new bleWriteRunnable(bleDevice, FORA_P80.SERVICE_UUID_STRING, FORA_P80.CHARACTEIRSTIC_UUID_STRING, FORA_P80.FORA_TURN_OFF_CMD), 500);
                        }
                    }
                });
    }

    private class bleWriteRunnable implements Runnable {
        private byte[] cmd;
        private BleDevice bleDevice;
        private String service;
        private String characteristic;

        public bleWriteRunnable(BleDevice bleDevice, String service, String characteristic, byte[] cmd) {
            this.bleDevice = bleDevice;
            this.cmd = cmd;
            this.service = service;
            this.characteristic = characteristic;
        }

        @Override
        public void run() {

            BleManager.getInstance().write(
                    bleDevice,
                    service,
                    characteristic,
                    cmd,
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            Log.d(TAG, exception.toString());
                        }
                    });
        }
    }

    private class bleTimeOutRunnable implements Runnable {

        @Override
        public void run() {
            Log.d(TAG, "disconnect");
            BleManager.getInstance().disconnect(bleDevice);
        }
    }
}
