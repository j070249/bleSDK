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
import com.dindon.ble.deviceBleUtils.FORA_GD40Utils;
import com.dindon.ble.deviceBleUtils.FORA_IR40Utils;
import com.dindon.ble.deviceBleUtils.FORA_P60Utils;
import com.dindon.ble.deviceBleUtils.FORA_TD8255Utils;
import com.dindon.ble.deviceBleUtils.FORA_W310Utils;
import com.dindon.ble.deviceItems.BaseDevice;
import com.dindon.ble.deviceItems.HOBO_MX1101;
import com.dindon.ble.deviceItems.IWEECARE_TEMPPAL;
import com.dindon.ble.deviceItems.MITEMP;
import com.dindon.ble.deviceItems.ROSSMAX_BGM;
import com.dindon.ble.deviceItems.ROSSMAX_BPM;
import com.dindon.ble.deviceItems.ROSSMAX_SP;
import com.dindon.ble.deviceItems.ROSSMAX_TM;
import com.dindon.ble.deviceItems.ROSSMAX_WF;
import com.dindon.ble.deviceItems.WC;
import com.dindon.ble.deviceItems.WP;
import com.viwaveulife.vuioht.VUBleDevice;
import com.viwaveulife.vuioht.VUBleManager;
import com.viwaveulife.vuioht.VUBleScanFilter;
import com.viwaveulife.vuioht.VUError;
import com.viwaveulife.vuioht.VUScanCallback;
import com.viwaveulife.vuioht.model.device_data.AbsDeviceData;
import com.viwaveulife.vuioht.model.device_data.VUBloodPressure;
import com.viwaveulife.vuioht.model.device_data.VUGlucose;
import com.viwaveulife.vuioht.model.device_data.VUPulseOximetry;
import com.viwaveulife.vuioht.model.device_data.VUTemperature;
import com.viwaveulife.vuioht.model.device_data.VUWeight;

import java.util.ArrayList;
import java.util.Arrays;
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
    private connectRossmaxCallback connectRossmaxCallback;
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
    byte[] dataBattery = new byte[18];
    byte[] dataHandT = new byte[21];

    private void startConnect(BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new bleGattCallback());
    }

    public void startConnectRossmax(final BaseDevice baseDevice, connectRossmaxCallback connectRossmaxCallback) {
//        stopScan();
        this.connectRossmaxCallback = connectRossmaxCallback;
        VUBleManager.getInstance().init(application, onSDKInitFinishListener);
        VUBleManager.getInstance().setScanCallback(new VUScanCallback() {
            @Override
            public void onScanResult(VUBleDevice vuBleDevice) {
                super.onScanResult(vuBleDevice);
                if (vuBleDevice.getName().contains(baseDevice.getName())) {
                    VUBleManager.getInstance().addObserveDevice(vuBleDevice.getId(), vuBleDevice);
                    VUBleManager.getInstance().connect(vuBleDevice, 10);
                }
            }

            @Override
            public void onScanFinish() {
                super.onScanFinish();
            }

            @Override
            public void onScanFailed(int i) {
                super.onScanFailed(i);
            }
        });
        VUBleManager.getInstance().setOnConnectionStateChangeListener(onConnectionStateChangeListener);
        VUBleManager.getInstance().setOnMeasurementReceiveListener(onMeasurementReceiveListener);
        ArrayList<VUBleScanFilter> scanFilterList = new ArrayList<>();
        VUBleScanFilter.Builder builder = new VUBleScanFilter.Builder();
        builder.setDeviceName("LBUart16Byte");
        scanFilterList.addAll(VUBleScanFilter.generateAllDeviceScanFilterList());
        scanFilterList.add(builder.build());
        VUBleManager.getInstance().startScan(scanFilterList, 0);
    }

    public void disConnectRossmax() {
        VUBleManager.getInstance().terminate();
//        startScan(scanResultCallback);
    }

    public boolean checkIsRossmax(Integer Type) {
        if (Type >= 0x61 && Type < 0x70)
            return true;
        else
            return false;
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
            if (bleDevice.getName() != null) {
                if (bleDevice.getName().equals("WP2")) {
                    scanResultCallback.onScanning(Config.WP, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                    byte[] data = ble_advdata_decode((byte) 0x88, bleDevice.getScanRecord());
                    if (data.length == 9)
                        dataResultCallback.onSuccess(new WP(bleDevice.getName(), bleDevice.getMac(), data));
                }
                if (bleDevice.getName().contains("WC")) {
                    scanResultCallback.onScanning(Config.WC, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                    byte[] data = ble_advdata_decode((byte) 0x88, bleDevice.getScanRecord());
                    if (data.length == 1)
                        dataResultCallback.onSuccess(new WC(bleDevice.getName(), bleDevice.getMac(), data));
                }
                if (bleDevice.getName().equals("MJ_HT_V1")) {
                    scanResultCallback.onScanning(Config.MITEMP, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                    byte[] data = ble_advdata_decode((byte) 0x16, bleDevice.getScanRecord());
                    if (data.length == 18) {
                        dataBattery = data;
                    }
                    if (data.length == 21) {
                        dataHandT = data;
                    }
                    if (dataBattery[0] != 0 && dataHandT[0] != 0) {
                        dataResultCallback.onSuccess(new MITEMP(bleDevice.getName(), bleDevice.getMac(), dataBattery, dataHandT));
                    }
                }
                if (bleDevice.getName().contains("iWEECARE Temp Pal")) {
                    scanResultCallback.onScanning(Config.IWEECARE_TEMPPAL, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                    byte[] data = ble_advdata_decode((byte) 0xff, bleDevice.getScanRecord());
                    Log.d(TAG, Arrays.toString(data));
                    dataResultCallback.onSuccess(new IWEECARE_TEMPPAL(bleDevice.getName(), bleDevice.getMac(), data));
                }
            }
            byte[] data = ble_advdata_decode((byte) 0xFF, bleDevice.getScanRecord());
            if (data.length >= 2) {
                if (data[0] == (byte) 0xC5 && data[1] == 0x00) {
                    if ((data[8] << 8 | data[7]) != 9218)
                        return;
                    scanResultCallback.onScanning(Config.HOBO_MX1101, new BaseDevice("noName", bleDevice.getMac()));
                    dataResultCallback.onSuccess(new HOBO_MX1101("noName", bleDevice.getMac(), data));
                }
            }
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
            if (bleDevice.getName().equals("FORA P60")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.FORA_P60, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                startConnect(bleDevice);
            }
            if (bleDevice.getName().equals("TAIDOC TD8255")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.FORA_TD8255, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                startConnect(bleDevice);
            }
            if (bleDevice.getName().equals("FORA IR40")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.FORA_IR40, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                startConnect(bleDevice);
            }
            if (bleDevice.getName().equals("FORA GD40")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.FORA_GD40, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                startConnect(bleDevice);
            }
            if (bleDevice.getName().equals("FORA W310")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.FORA_W310, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
                startConnect(bleDevice);
            }
            if (bleDevice.getName().contains("RM_BPM") || bleDevice.getName().contains("ROSSMAX")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.ROSSMAX_RM_BPM, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
            }
            if (bleDevice.getName().contains("TBMT") || bleDevice.getName().contains("Tysonbio_BGM")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.ROSSMAX_RM_BGM, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
            }
            if (bleDevice.getName().contains("RM_FH") || bleDevice.getName().contains("SLBLE")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.ROSSMAX_RM_FH, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
            }
            if (bleDevice.getName().contains("RM_SP") || bleDevice.getName().contains("LBUart16Byte")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.ROSSMAX_RM_SP, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
            }
            if (bleDevice.getName().contains("LS212")) {
                Log.d(TAG, "bleDevice :" + bleDevice.getName());
                scanResultCallback.onScanning(Config.ROSSMAX_RM_WF, new BaseDevice(bleDevice.getName(), bleDevice.getMac()));
            }

        }
    }

    private byte[] ble_advdata_decode(byte type, byte[] data) {
        int index = 0;
        int field_length, field_type;
        byte[] decode_data;

        while (index < data.length) {
            field_length = data[index];
            field_type = data[index + 1];

            if (field_type == type) {
                decode_data = new byte[field_length];
                System.arraycopy(data, index + 2, decode_data, 0, (field_length - 1));
                return decode_data;
            }
            if (field_type == 0x00)
                break;
            index += field_length + 1;
        }
        return new byte[0];
    }

    private class bleGattCallback extends BleGattCallback {

        @Override
        public void onStartConnect() {
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            dataResultCallback.onFailure(new GtechException(GtechException.CONNECT_DEVICE_FAILD, String.format("connect to %s device failed", bleDevice.getName())));
//            BleManager.getInstance().disconnectAllDevice();
            startScan(scanResultCallback);
        }

        @Override
        public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
            dataResultCallback.onFailure(new GtechException(SUCCESS, String.format("connect to %s device success", bleDevice.getName())));
            stopScan();
            if (bleDevice.getName().equals("FORA D40"))
                new FORA_D40Utils(bleDevice, gatt, dataResultCallback);
            if (bleDevice.getName().equals("FORA P60"))
                new FORA_P60Utils(bleDevice, gatt, dataResultCallback);
            if (bleDevice.getName().equals("TAIDOC TD8255"))
                new FORA_TD8255Utils(bleDevice, gatt, dataResultCallback);
            if (bleDevice.getName().equals("FORA IR40"))
                new FORA_IR40Utils(bleDevice, gatt, dataResultCallback);
            if (bleDevice.getName().equals("FORA GD40"))
                new FORA_GD40Utils(bleDevice, gatt, dataResultCallback);
            if (bleDevice.getName().equals("FORA W310"))
                new FORA_W310Utils(bleDevice, gatt, dataResultCallback);
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            dataResultCallback.onFailure(new GtechException(GtechException.DISCONNECT_FROM_DEVICE, String.format("disconnect from %s device", device.getName())));
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

    private VUBleManager.VUOnSDKInitFinishListener onSDKInitFinishListener = new VUBleManager.VUOnSDKInitFinishListener() {
        @Override
        public void onSdkInitFinish(int statusCode, String reason) {
            if (statusCode != VUBleManager.SDK_INIT_SUCCESS) {
                Log.d("TEST", "Init SDK error ");
                throw new RuntimeException("Init SDK error " + reason);
            }
        }
    };

    private VUBleManager.VUOnConnectionStateChangeListener onConnectionStateChangeListener = new VUBleManager.VUOnConnectionStateChangeListener() {

        @Override
        public void onConnectionReady(VUBleDevice vuBleDevice) {
            connectRossmaxCallback.onConnectionReady(new GtechException(SUCCESS, String.format("connect to %s device success", vuBleDevice.getName())));
        }

        @Override
        public void onTimeoutExpired(VUBleDevice vuBleDevice) {
            connectRossmaxCallback.onTimeoutExpired(new GtechException(GtechException.CONNECT_DEVICE_FAILD, String.format("connect to %s device failed", vuBleDevice.getName())));

        }

        @Override
        public void onDisconnected(VUBleDevice vuBleDevice, int i) {
            connectRossmaxCallback.onDisconnected(new GtechException(GtechException.CONNECT_DEVICE_FAILD, String.format("connect to %s device failed", vuBleDevice.getName())));
        }
    };

    private VUBleManager.VUOnMeasurementReceiveListener onMeasurementReceiveListener = new VUBleManager.VUOnMeasurementReceiveListener() {
        @Override
        public void onGlucoseReceive(VUBleDevice vuBleDevice, VUGlucose vuGlucose, VUError vuError) {
            dataResultCallback.onSuccess(new ROSSMAX_BGM(vuBleDevice.getName(), vuBleDevice.getId(), vuGlucose.getDate().toString(), vuGlucose.getGlucose_mg_dL()));
        }

        @Override
        public void onPulseOximetryReceive(VUBleDevice vuBleDevice, VUPulseOximetry vuPulseOximetry, VUError vuError) {
            dataResultCallback.onSuccess(new ROSSMAX_SP(vuBleDevice.getName(), vuBleDevice.getId(), vuPulseOximetry.getDate().toString(), vuPulseOximetry.getPulse(), vuPulseOximetry.getSpO2()));
        }

        @Override
        public void onBloodPressureReceive(VUBleDevice vuBleDevice, VUBloodPressure vuBloodPressure, VUError vuError) {
            dataResultCallback.onSuccess(new ROSSMAX_BPM(vuBleDevice.getName(), vuBleDevice.getId(), vuBloodPressure.getDate().toString(), vuBloodPressure.getSystolic(), vuBloodPressure.getDiastolic(), vuBloodPressure.getPulse()));
        }

        @Override
        public void onBloodPressureIntermediateCuffReceive(VUBleDevice vuBleDevice, int i) {

        }

        @Override
        public void onTemperatureReceive(VUBleDevice vuBleDevice, VUTemperature vuTemperature, VUError vuError) {
            dataResultCallback.onSuccess(new ROSSMAX_TM(vuBleDevice.getName(), vuBleDevice.getId(), vuTemperature.getTemperatureC(), vuTemperature.getDate().toString()));
        }

        @Override
        public void onWeightReceive(VUBleDevice vuBleDevice, VUWeight vuWeight, VUError vuError) {
            dataResultCallback.onSuccess(new ROSSMAX_WF(vuBleDevice.getName(), vuBleDevice.getId(), vuWeight.getDate().toString(), vuWeight.getWeight(), vuWeight.getVisceralFat(), vuWeight.getProteinContent(), vuWeight.getMuscleMass(), vuWeight.getBoneDensity(), vuWeight.getBodyWater(), vuWeight.getBodyFat(), vuWeight.getBMI(), vuWeight.getBasalMetabolism()));
            ;
        }

        @Override
        public void onHistoryDataReceive(VUBleDevice vuBleDevice, List<? extends AbsDeviceData> list, Class aClass) {

        }

        @Override
        public void onProgressUpdate(VUBleDevice vuBleDevice, int i, int i1) {

        }

        @Override
        public void onBloodPressureRawDataReceive(VUBleDevice vuBleDevice, int i, int i1, int i2) {

        }

        @Override
        public void onPulseOximetryPPGDataReceive(VUBleDevice vuBleDevice, ArrayList<Double> arrayList, VUError vuError) {

        }

        @Override
        public void onSetDateTimeFinish(VUBleDevice vuBleDevice, boolean b) {

        }
    };
}
