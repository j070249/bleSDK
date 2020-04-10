package com.dindon.gtech_ble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;


import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

import com.dindon.ble.GtechBle;
import com.dindon.ble.GtechException;
import com.dindon.ble.deviceItems.BaseDevice;
import com.dindon.ble.deviceItems.FORA_D40;
import com.dindon.ble.deviceItems.FORA_IR40;
import com.dindon.ble.deviceItems.FORA_P80;

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
            Log.d(TAG, "bleDevice :" + baseDevice.getName());
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
            if (device instanceof FORA_IR40) {
                FORA_IR40 fora_ir40 = ((FORA_IR40) device);
                TextView fieldsuggest = (TextView)findViewById(R.id.textView);
                String stringValue = Double.toString(fora_ir40.getObjectTemperature()/10.0);
                fieldsuggest.setText(stringValue);
                fieldsuggest.setTextSize(28.0f);
                fieldsuggest.setTextColor(0xFFFF0000);
                Log.d(TAG, String.format("%s => ObjectTemperature = %d, AmbientTemperature = %d", fora_ir40.getDataTime(), fora_ir40.getObjectTemperature(), fora_ir40.getAmbientTemperature()));
            }
            if (device instanceof FORA_P80) {
                FORA_P80 fora_p80 = ((FORA_P80) device);
                Log.d(TAG, String.format("%s => sys = %d, dia = %d, pulse = %d", fora_p80.getDataTime(), fora_p80.getSys(), fora_p80.getDia(), fora_p80.getPulse()));
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
