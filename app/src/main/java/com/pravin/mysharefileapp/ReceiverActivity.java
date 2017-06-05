package com.pravin.mysharefileapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.pravin.mysharefileapp.file_share.FileReceiver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

public class ReceiverActivity extends AppCompatActivity {

    WifiManager wifiManager;
    FileReceiver fileReceiver;
    TextView tvCode;
    ProgressDialog progressDoalog;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FileReceiver.CODE :
                    Log.e("hete","here");
                    tvCode.setText((int)msg.obj + "");
                    Log.e("check",msg.obj+"");

                    break;
                case FileReceiver.RECEIVE_PROCESS :
                   // tvCode.setText((int)msg.obj + "");
                      if(!progressDoalog.isShowing()){
                        progressDoalog.show();
                    }
                    progressDoalog.setProgress((int)msg.obj);
                    break;
                case FileReceiver.LISTENING :
                    Toast.makeText(ReceiverActivity.this,"Listening...",Toast.LENGTH_SHORT).show();
                    break;

                case FileReceiver.CONNECTED:
                    Toast.makeText(ReceiverActivity.this,"Connected!",Toast.LENGTH_SHORT).show();
                    break;

                case FileReceiver.RECEIVING_FILE :
                    Toast.makeText(ReceiverActivity.this,"Receiving File!",Toast.LENGTH_SHORT).show();
                    break;

                case FileReceiver.FILE_RECEIVED :
                    File file = (File) msg.obj;
                    progressDoalog.dismiss();
                    Toast.makeText(ReceiverActivity.this,file.getName() + " Received!",Toast.LENGTH_SHORT).show();
                    Toast.makeText(ReceiverActivity.this,"Stored in " + file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ReceiverActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    fileReceiver.close();
                    break;

                case FileReceiver.RECEIVE_ERROR :
                    Toast.makeText(ReceiverActivity.this,"Error occured : " + (String)msg.obj,Toast.LENGTH_SHORT).show();
                    //fileReceiver.close();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        tvCode=(TextView)findViewById(R.id.tvCode);
        progressDoalog = new ProgressDialog(ReceiverActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Receiving....");
        progressDoalog.setTitle("Receiving File");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);




        wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }

        WifiConfiguration netConfig = new WifiConfiguration();
        String hostpostName=PreferenceManager.getInstance().getStringValue(PreferenceManager.USER_NAME)+"MyShareApp";
        byte[] data = new byte[0];
        String base64="";
        try {
            data = hostpostName.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        netConfig.SSID = base64;
        netConfig.preSharedKey="123";
       // netConfig.BSSID="HOTSPOT";
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//        netConfig.providerFriendlyName="PRAVIN";
       // netConfig.networkId=1111;
        try{
            Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            boolean apstatus=(Boolean) setWifiApMethod.invoke(wifiManager, netConfig,true);

            Method isWifiApEnabledmethod = wifiManager.getClass().getMethod("isWifiApEnabled");
            while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager)){

            };
            Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
            int apstate=(Integer)getWifiApStateMethod.invoke(wifiManager);
            Method getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            netConfig=(WifiConfiguration)getWifiApConfigurationMethod.invoke(wifiManager);
            Log.e("CLIENT", "\nSSID:"+netConfig.SSID+"\nPassword:"+netConfig.preSharedKey+"\n FQDN "+netConfig.FQDN+"\n");

        } catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
        }

        fileReceiver = new FileReceiver(this,mHandler);

        fileReceiver.getFile();
    }
}
