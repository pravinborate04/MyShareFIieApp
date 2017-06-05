package com.pravin.mysharefileapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button btnReceive, btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnReceive = (Button) findViewById(R.id.btnReceive);
        if (TextUtils.isEmpty(PreferenceManager.getInstance().getStringValue(PreferenceManager.USER_NAME))) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_select_username);
            dialog.setCancelable(false);
            final EditText edtUsername = (EditText) dialog.findViewById(R.id.edtUsername);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(edtUsername.getText().toString())) {
                        edtUsername.setError("Please enter username");
                        edtUsername.requestFocus();
                        return;
                    } else {
                        dialog.dismiss();
                        PreferenceManager.getInstance().saveStringValue(PreferenceManager.USER_NAME, edtUsername.getText().toString());
                    }
                }
            });
            dialog.show();
        }


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);
                startActivity(new Intent(MainActivity.this, SenderActivity.class));

            }
        });


        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReceiverActivity.class));
            }
        });

    }


   /* public boolean checkWifiStatus(){
        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()){
            return true;
        }else {
            return false;

        }
    }*/
}
