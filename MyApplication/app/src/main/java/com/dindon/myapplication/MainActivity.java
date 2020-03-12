package com.dindon.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dindon.ble.GtechBle;
import com.dindon.ble.GtechException;
import com.dindon.ble.deviceItems.BaseDevice;
import com.dindon.ble.deviceItems.FORA_D40;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GtechBle GtechBle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GtechBle = new GtechBle(getApplication(), new GtechBle.dataResultCallback() {
            @Override
            public void onSuccess(BaseDevice baseDevice) {

            }

            @Override
            public void onFailure(GtechException e) {

            }
        });
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        GtechException ge = GtechBle.startScan(new GtechBle.scanResultCallback() {
            @Override
            public void onScanning(Integer integer, BaseDevice baseDevice) {

            }

            @Override
            public void onResult(GtechException e) {

            }
        });
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
            Log.d(TAG, "onSuccess");
            if (device instanceof FORA_D40) {
                FORA_D40 fora_d40 = ((FORA_D40) device);
                if (fora_d40.getDataType() == 1) {
                    Log.d(TAG, String.format("%s => sys = %d, dia = %d, pulse = %d", fora_d40.getDataTime(), fora_d40.getSys(), fora_d40.getDia(), fora_d40.getPulse()));
                } else if (fora_d40.getDataType() == 2) {
                    Log.d(TAG, String.format("%s => mode = %d, glucose = %d", fora_d40.getDataTime(), fora_d40.getMode(), fora_d40.getGlucose()));
                }
            }
        }

        @Override
        public void onFailure(GtechException ge) {
            Log.d(TAG, ge.toString());
        }
    }
}
