package com.dindon.ble;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.dindon.ble.deviceBleUtils.FORA_D40Utils;
import com.dindon.ble.deviceBleUtils.FORA_IR40Utils;
import com.dindon.ble.deviceBleUtils.FORA_P80Utils;
import com.dindon.ble.deviceItems.BaseDevice;

import java.util.List;

import static com.dindon.ble.GtechException.ON_SCAN_FINISHED;
import static com.dindon.ble.GtechException.ON_SCAN_STARTED;
import static com.dindon.ble.GtechException.REQUEST_ACCESS_FINE_LOCATION_PERMISSION;
import static com.dindon.ble.GtechException.REQUEST_BLE_OPEN;
import static com.dindon.ble.GtechException.REQUEST_GPS_OPEN;
import static com.dindon.ble.GtechException.SUCCESS;

/**
 * Created by G-Tech on 2019/8/12.
 * E-mail: j070249@gmail.com
 */

public class GtechBle {

    private static final String TAG = "BLE_LIBRARY";

    private dataResultCallback dataResultCallback;
    private scanResultCallback scanResultCallback;
    private Application application;

    /**
     * 初始化設定
     *
     * @param application application from activity
     * @param callback    ble data callback
     */


    public GtechBle(Application application, dataResultCallback callback) {
        BleManager.getInstance().init(application);

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(0)
                .build();

        BleManager.getInstance()
                .enableLog(false)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(10000)
                .setOperateTimeout(6000)
                .initScanRule(scanRuleConfig);

        this.dataResultCallback = callback;
        this.application = application;
    }

    public interface dataResultCallback {
        void onSuccess(BaseDevice device);

        void onFailure(GtechException ge);
    }

    public interface scanResultCallback {
        void onScanning(Integer Type, BaseDevice baseDevice);

        void onResult(GtechException ge);
    }

    public interface connectRossmaxCallback {
        void onConnectionReady(GtechException ge);

        void onTimeoutExpired(GtechException ge);

        void onDisconnected(GtechException ge);
    }

    /**
     * 執行ble搜尋
     *
     * @param callback
     * @return
     */

    public GtechException startScan(scanResultCallback callback) {

        if (checkPermissions().getCode() == SUCCESS) {
            BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                    .setScanTimeOut(0)
                    .build();
            BleManager.getInstance().initScanRule(scanRuleConfig);
            BleManager.getInstance().scan(new bleScanCallback());
            this.scanResultCallback = callback;
            return checkPermissions();
        } else {

            return checkPermissions();
        }
    }

    /**
     * 停止ble搜尋
     *
     * @return
     */

    public boolean stopScan() {
        if (isScanStart)
            BleManager.getInstance().cancelScan();
        return true;
    }

    /**
     * 連接bleDevice
     *
     * @param bleDevicescanResultCallback
     * @return
     */

    boolean isScanStart = false;

    private void startConnect(BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new bleGattCallback());
    }

    private class bleScanCallback extends BleScanCallback {

        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            isScanStart = false;
            Log.d(TAG, "onScanFinished");
            scanResultCallback.onResult(new GtechException(ON_SCAN_FINISHED, "onScanFinished"));
        }

        @Override
        public void onLeScan(BleDevice bleDevice) {
        }

        @Override
        public void onScanStarted(boolean success) {
            isScanStart = true;
            scanResultCallback.onResult(new GtechException(ON_SCAN_STARTED, "onScanStarted"));
        }

        @Override
        public void onScanning(BleDevice bleDevice) {
            if (bleDevice.getName() == null)
                return;
            if (bleDevice.getName().equals("FORA D40")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.FORA_D40, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                startConnect(bleDevice);
            }
            if (bleDevice.getName().equals("FORA IR40")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.FORA_IR40, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                startConnect(bleDevice);
            }
            if (bleDevice.getName().equals("DIAMOND CUFF BP")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.FORA_P80, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                startConnect(bleDevice);
            }
        }
    }

    private class bleGattCallback extends BleGattCallback {

        @Override
        public void onStartConnect() {
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            dataResultCallback.onFailure(new GtechException(GtechException.CONNECT_DEVICE_FAILD, String.format("connect to %s device failed", bleDevice.getName())));
            BleManager.getInstance().disconnectAllDevice();
            startScan(scanResultCallback);
        }

        @Override
        public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
            dataResultCallback.onFailure(new GtechException(SUCCESS, String.format("connect to %s device success", bleDevice.getName())));
            stopScan();
            if (bleDevice.getName().equals("FORA D40"))
                new FORA_D40Utils(bleDevice, gatt, dataResultCallback);
            if (bleDevice.getName().equals("FORA IR40"))
                new FORA_IR40Utils(bleDevice, gatt, dataResultCallback);
            if (bleDevice.getName().equals("DIAMOND CUFF BP"))
                new FORA_P80Utils(bleDevice, gatt, dataResultCallback);
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            dataResultCallback.onFailure(new GtechException(GtechException.DISCONNECT_FROM_DEVICE, String.format("disconnect from %s device", device.getName())));
            BleManager.getInstance().disconnectAllDevice();
            startScan(scanResultCallback);
        }
    }

    private GtechException checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            //要求開啟藍芽
            return new GtechException(REQUEST_BLE_OPEN, "please open the bluetooth");
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(application, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                switch (permission) {
                    case Manifest.permission.ACCESS_FINE_LOCATION:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                            //要求開啟GPS
                            return new GtechException(REQUEST_GPS_OPEN, "please open the GPS");
                        } else {
                            //開始運作
                            return new GtechException(SUCCESS, "check permission success");
                        }
                }
            } else {
                //要求開啟權限
                return new GtechException(REQUEST_ACCESS_FINE_LOCATION_PERMISSION, "please open location permission");
            }
        }
        return new GtechException(SUCCESS, "success");
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }
}
