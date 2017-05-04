package com.pravin.mysharefileapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;



public class WifiOnlyBroadCastReceiver extends BroadcastReceiver {

    WifiManager mWifiManager;
    NewWifiDeviceListener listener;
    Context context;
    public static final String TAG=WifiOnlyBroadCastReceiver.class.getSimpleName();
    public WifiOnlyBroadCastReceiver(WifiManager wifiManager, NewWifiDeviceListener newWifiDeviceListener,Context context){
        this.mWifiManager=wifiManager;
        this.listener=newWifiDeviceListener;
        this.context=context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: " );
        Log.d(TAG,intent.getAction());
        String action=intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            List<ScanResult> devices=new ArrayList<>();
            Log.d(TAG, "onReceive: inside" );
            List<ScanResult> mScanResults = mWifiManager.getScanResults();
                        // add your logic here
            Log.d("SIZE",mWifiManager.getScanResults()+"");
            for (ScanResult scanResult:mScanResults){
               // Log.d(TAG, "onReceive: "+scanResult.SSID);
              //  Log.d(TAG, "onReceive: "+scanResult);
                if(!TextUtils.isEmpty(scanResult.SSID)){
                  //  Device device=new Device(scanResult.SSID);
                    String capabilities = scanResult.capabilities;
                    //Log.w(TAG, scanResult.SSID + " capabilities : " + capabilities);
                    if (capabilities.toUpperCase().contains("WEP")) {
                        // WEP Network
                    } else if (capabilities.toUpperCase().contains("WPA")
                            || capabilities.toUpperCase().contains("WPA2")) {
                        // WPA or WPA2 Network
                    } else {
                        // Open Network
                        Log.e("our device",scanResult.toString());
                        devices.add(scanResult);
                    }

                }
            }

            listener.newDevices(devices);
        }

        if(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)){
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            Log.e("state",state.toString());
            if (SupplicantState.isValidState(state)
                    && state == SupplicantState.COMPLETED) {

            }
        }
    }
}
