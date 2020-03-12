# GtechBle
Integrated BLE device SDK

Current support device items:

### FORA
 - FORA D40(TD-3261A)血糖血壓機
 - FORA TD8255(TD-8255)血氧濃度機

# Usage

- #### Permission

		<uses-permission android:name="android.permission.BLUETOOTH" />
		<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
		<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
		<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

- #### Init

        GtechBle = new GtechBle(getApplication(), new GtechBle.dataResultCallback() {
            @Override
            public void onSuccess(BaseDevice device) {
				/*
				get ble device data here
				*/
            }

            @Override
            public void onFailure(GtechException e) {
				/*
				e.getCode() == GtechException.CONNECT_DEVICE_FAILD
				e.getCode() == GtechException.DISCONNECT_FROM_DEVICE
				*/
            }
        });

- #### StartScan

		GtechException ge = GtechBle.startScan(new GtechBle.scanResultCallback() {
            @Override
            public void onScanning(Integer integer, BaseDevice baseDevice) {
                Log.d(TAG, "bleDevice :" + baseDevice.getName());
                if (GtechBle.checkIsRossmax(Type)) {
                    /*
                    It is recommended to present in a list to give the user a decision on whether to connect
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
                    */
                }
            }

            @Override
            public void onResult(GtechException e) {
				/*
				e.getCode() == GtechException.ON_SCAN_FINISHED
				e.getCode() == GtechException.ON_SCAN_STARTED
				*/
            }
        });

		
	Tips:
	You must confirm here that the app has permission to open, otherwise it will not work.
	- ge.getCode() == GtechException.SUCCESS
	Successfully turned on scanning
	- ge.getCode() == GtechException.REQUEST_BLE_OPEN
	Request to enable Bluetooth
		
	- ge.getCode() == GtechException.REQUEST_GPS_OPEN
	Request to enable GPS
		
	- ge.getCode() == GtechException.REQUEST_ACCESS_FINE_LOCATION_PERMISSION
	Request to enable LOCATION PERMISSION

- #### StopScan

		GtechBle.stopScan();
		
- #### BaseDevice

	BaseDevice have to use instanceof to define the ble device
	
	Example:
	
		if (device instanceof FORA_D40) {
            FORA_D40 fora_d40 = ((FORA_D40) device);
            if (fora_d40.getDataType() == 1) {
                Log.d(TAG, String.format("%s => sys = %d, dia = %d, pulse = %d", fora_d40.getDataTime(), fora_d40.getSys(), fora_d40.getDia(), fora_d40.getPulse()));
            } else if (fora_d40.getDataType() == 2) {
                Log.d(TAG, String.format("%s => mode = %d, glucose = %d", fora_d40.getDataTime(), fora_d40.getMode(), fora_d40.getGlucose()));
            }
        }
        
    
        
	- FORA_D40 (FORA D40(TD-3261A)血糖血壓機)
	
	`String getDataTime()`
	
	`int getDataType()`
	
	if Type == 1 is blood pressure
	
	`int getSys()`
	
	`int getDia()`
	
	`int getPulse()`
	
	if Type == 2 is blood glucose
	
	`int geMode()` 0 = "飯前", 1 = "飯前", 2 = "飯後", 3 = "品管"
	
	`int getGlucose()`
	
	- FORA_P60 (FORA P60(TD-3128B)血壓機)
	
	`String getDataTime()`
	
	`int getSys()`
	
	`int getDia()`
	
	`int getPulse()`

	- FORA_IR40 (FORA IR40(TD-1241)額溫機)
	
	`String getDataTime()`
	
	`int getObjectTemperature()`
	
	`int getAmbientTemperature()`
	

- #### GtechException

    `int getCode()` error code

    `String getDescription()` error description

    `String toString()` Debug