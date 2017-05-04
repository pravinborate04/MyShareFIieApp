package com.pravin.mysharefileapp;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class SenderActivity extends AppCompatActivity implements NewWifiDeviceListener{

    RecyclerView lstWifi;

    WifiManager mWifiManager;
    WifiOnlyBroadCastReceiver mWifiScanReceiver;
    WifiAdapter wifiAdapter;
    List<ScanResult> deviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        lstWifi=(RecyclerView)findViewById(R.id.lstWifi);
        lstWifi.setLayoutManager(new LinearLayoutManager(this));
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        deviceList=new ArrayList<>();
        wifiAdapter=new WifiAdapter(this,deviceList);
        lstWifi.setAdapter(wifiAdapter);
        mWifiManager.startScan();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mWifiScanReceiver=new WifiOnlyBroadCastReceiver(mWifiManager,this,this);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(mWifiScanReceiver,
                intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mWifiScanReceiver);

    }


    @Override
    public void newDevices(List<ScanResult> devices) {
        Log.e("callbac","callback");
        deviceList.clear();
        wifiAdapter.notifyDataSetChanged();
        deviceList.addAll(devices);
        wifiAdapter.notifyDataSetChanged();
    }

    @Override
    public void connected(boolean connected) {
        Toast.makeText(this, "Connected "+connected, Toast.LENGTH_SHORT).show();
    }


}
