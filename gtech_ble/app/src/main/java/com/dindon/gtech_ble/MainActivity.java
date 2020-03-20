package com.dindon.gtech_ble;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dindon.ble.GtechBle;
import com.dindon.ble.GtechException;
import com.dindon.ble.deviceItems.BaseDevice;
import com.dindon.ble.deviceItems.FORA_D40;
import com.dindon.ble.deviceItems.FORA_GD40;
import com.dindon.ble.deviceItems.FORA_IR40;
import com.dindon.ble.deviceItems.FORA_P60;
import com.dindon.ble.deviceItems.FORA_TD8255;
import com.dindon.ble.deviceItems.FORA_W310;
import com.dindon.ble.deviceItems.HOBO_MX1101;
import com.dindon.ble.deviceItems.MITEMP;
import com.dindon.ble.deviceItems.ROSSMAX_BGM;
import com.dindon.ble.deviceItems.ROSSMAX_BPM;
import com.dindon.ble.deviceItems.ROSSMAX_SP;
import com.dindon.ble.deviceItems.ROSSMAX_TM;
import com.dindon.ble.deviceItems.ROSSMAX_WF;
import com.dindon.ble.deviceItems.WC;
import com.dindon.ble.deviceItems.WP;
import com.viwaveulife.vuioht.VUBleManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GtechBle GtechBle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GtechBle = new GtechBle(getApplication(), new dataResultCallback());
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        GtechException ge = GtechBle.startScan(new scanResultCallback());
        Log.d(TAG, ge.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        GtechBle.stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GtechBle.stopScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GtechBle.stopScan();
    }

    private class scanResultCallback implements GtechBle.scanResultCallback {

        @Override
        public void onScanning(Integer Type, BaseDevice baseDevice) {
//            Log.d(TAG, "bleDevice :" + baseDevice.getName());
            if (GtechBle.checkIsRossmax(Type)) {
                Log.d(TAG, "bleDevice :" + baseDevice.getName());
                GtechBle.startConnectRossmax(baseDevice, new GtechBle.connectRossmaxCallback() {
                    @Override
                    public void onConnectionReady(GtechException ge) {

                    }

                    @Override
                    public void onTimeoutExpired(GtechException ge) {

                    }

                    @Override
                    public void onDisconnected(GtechException ge) {

                    }
                });
            }
        }

        @Override
        public void onResult(GtechException ge) {
            Log.d(TAG, ge.toString());
        }
    }

    private class dataResultCallback implements GtechBle.dataResultCallback {

        @Override
        public void onSuccess(BaseDevice device) {
//            Log.d(TAG, "onSuccess" + device.getName());
            if (device instanceof FORA_D40) {
                FORA_D40 fora_d40 = ((FORA_D40) device);
                if (fora_d40.getDataType() == 1) {
                    Log.d(TAG, String.format("%s => sys = %d, dia = %d, pulse = %d", fora_d40.getDataTime(), fora_d40.getSys(), fora_d40.getDia(), fora_d40.getPulse()));
                } else if (fora_d40.getDataType() == 2) {
                    Log.d(TAG, String.format("%s => mode = %d, glucose = %d", fora_d40.getDataTime(), fora_d40.getMode(), fora_d40.getGlucose()));
                }
            }
            if (device instanceof FORA_P60) {
                FORA_P60 fora_p60 = ((FORA_P60) device);
                Log.d(TAG, String.format("%s => sys = %d, dia = %d, pulse = %d", fora_p60.getDataTime(), fora_p60.getSys(), fora_p60.getDia(), fora_p60.getPulse()));
            }
            if (device instanceof FORA_TD8255) {
                FORA_TD8255 fora_td8255 = ((FORA_TD8255) device);
                Log.d(TAG, String.format("%s => SpO2 = %d, pulse = %d", fora_td8255.getDataTime(), fora_td8255.getSpO2(), fora_td8255.getPulse()));
            }
            if (device instanceof FORA_IR40) {
                FORA_IR40 fora_ir40 = ((FORA_IR40) device);
                Log.d(TAG, String.format("%s => ObjectTemperature = %d, AmbientTemperature = %d", fora_ir40.getDataTime(), fora_ir40.getObjectTemperature(), fora_ir40.getAmbientTemperature()));
            }
            if (device instanceof FORA_GD40) {
                FORA_GD40 fora_gd40 = ((FORA_GD40) device);
                Log.d(TAG, String.format("%s => mode = %d, glucose = %d", fora_gd40.getDataTime(), fora_gd40.getMode(), fora_gd40.getGlucose()));
            }
            if (device instanceof FORA_W310) {
                FORA_W310 fora_w310 = ((FORA_W310) device);
                Log.d(TAG, String.format("%s => weight = %.2f, bf = %.2f", fora_w310.getDataTime(), fora_w310.getWeight(), fora_w310.getBodyFat()));
            }
            if (device instanceof WP) {
                WP wp = ((WP) device);
//                Log.d(TAG, String.format("WP-%s => event = %d, battery = %d", wp.getMac(), wp.getEvent(), wp.getBattery()));
            }
            if (device instanceof WC) {
                WC wc = ((WC) device);
                Log.d(TAG, String.format("WP-%s => event = %d", wc.getMac(), wc.getEvent()));
            }
            if (device instanceof MITEMP) {
                MITEMP mt = ((MITEMP) device);
                Log.d(TAG, String.format("MI-%s => Temp = %.2f, battery = %d", mt.getMac(), mt.getTemperature(), mt.getBattery()));
            }
            if (device instanceof HOBO_MX1101) {
                HOBO_MX1101 hb = ((HOBO_MX1101) device);
                Log.d(TAG, String.format("HOBO-%s => Temp = %.2f, battery = %.2f", hb.getMac(), hb.getTemperature(), hb.getBattery()));
            }
            if (device instanceof ROSSMAX_TM) {
                ROSSMAX_TM rossmax_TM = ((ROSSMAX_TM) device);
                Log.d(TAG, String.format("rossmax_TM-%s => Temp = %.2f", rossmax_TM.getMac(), rossmax_TM.getTemperature()));
            }
            if (device instanceof ROSSMAX_BPM) {
                ROSSMAX_BPM rossmax_BPM = ((ROSSMAX_BPM) device);
                Log.d(TAG, String.format("rossmax_BPM-%s => Sys = %d, Dia = %d, Pulse = %d", rossmax_BPM.getMac(), rossmax_BPM.getSys(), rossmax_BPM.getDia(), rossmax_BPM.getPulse()));
            }
            if (device instanceof ROSSMAX_BGM) {
                ROSSMAX_BGM rossmax_BGM = ((ROSSMAX_BGM) device);
                Log.d(TAG, String.format("rossmax_BGM-%s => Glucose = %d", rossmax_BGM.getMac(), rossmax_BGM.getGluecose()));
            }
            if (device instanceof ROSSMAX_SP) {
                ROSSMAX_SP rossmax_SP = ((ROSSMAX_SP) device);
                Log.d(TAG, String.format("rossmax_BGM-%s => SpO2 = %d, pulse = %d", rossmax_SP.getMac(), rossmax_SP.getSp02(), rossmax_SP.getPulse()));
            }
            if (device instanceof ROSSMAX_WF) {
                ROSSMAX_WF rossmax_WF = ((ROSSMAX_WF) device);
                Log.d(TAG, String.format("rossmax_WF-%s => Weight = %.2f, BodyFat = %.2f", rossmax_WF.getMac(), rossmax_WF.getWeight(), rossmax_WF.getBodyFat()));
            }
        }

        @Override
        public void onFailure(GtechException ge) {
            Log.d(TAG, ge.toString());
        }
    }


    private class connectRossmaxCallback implements GtechBle.connectRossmaxCallback {

        @Override
        public void onConnectionReady(GtechException ge) {

        }

        @Override
        public void onTimeoutExpired(GtechException ge) {

        }

        @Override
        public void onDisconnected(GtechException ge) {

        }
    }
}
