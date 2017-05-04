package com.pravin.mysharefileapp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;

public class SendToActivity extends AppCompatActivity {

    EditText txtPortNumber;
    Button btnNext;
    TextInputLayout textInputPort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to);
        //getClientList();
        textInputPort=(TextInputLayout)findViewById(R.id.textInputPort);
        btnNext=(Button)findViewById(R.id.btnNext);
        txtPortNumber=(EditText)findViewById(R.id.txtPortNumber);
        WifiManager mWiFiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo w = mWiFiManager.getConnectionInfo();
        Log.e("Check","APN Name = "+w.getSSID()+" BSSID "+w.getBSSID()+" PORT "+w.toString());
        //Toast.makeText(this, "APN Name = "+w.getSSID()+" BSSID "+w.getBSSID()+" PORT "+w.toString(), Toast.LENGTH_SHORT).show();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtPortNumber.getText().toString().trim())){
                    textInputPort.setError("Please enter port number");
                    txtPortNumber.requestFocus();
                    return;
                }else {
                    Intent intent=new Intent(SendToActivity.this,FileDirectoryActivity.class);
                    intent.putExtra("PORT",Integer.parseInt(txtPortNumber.getText().toString()));
                    startActivity(intent);
                }
            }
        });
    }


    public void getClientList() {
        int macCount = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            Log.e("reader",br.toString());
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null ) {
                    // Basic sanity check
                    String mac = splitted[3];
                    System.out.println("Mac : Outside If "+ mac );
                    if (mac.matches("..:..:..:..:..:..")) {
                        macCount++;
                   /* ClientList.add("Client(" + macCount + ")");
                    IpAddr.add(splitted[0]);
                    HWAddr.add(splitted[3]);
                    Device.add(splitted[5]);*/
                        System.out.println("Mac : "+ mac + " IP Address : "+splitted[0] );
                        System.out.println("Mac_Count  " + macCount + " MAC_ADDRESS  "+ mac);
                        Toast.makeText(
                                getApplicationContext(),
                                "Mac_Count  " + macCount + "   MAC_ADDRESS  "
                                        + mac, Toast.LENGTH_SHORT).show();

                    }
               /* for (int i = 0; i < splitted.length; i++)
                    System.out.println("Addressssssss     "+ splitted[i]);*/

                }
            }
        } catch(Exception e) {

        }
    }
}
