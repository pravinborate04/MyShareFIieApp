package com.pravin.mysharefileapp;

import android.net.wifi.ScanResult;


import java.util.List;



public interface NewWifiDeviceListener {

    public void newDevices(List<ScanResult> deviceList);
    public void connected(boolean connected);
}
