package com.pravin.mysharefileapp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.List;



public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.MyWifiViewHolder>{

    LayoutInflater layoutInflater;
    Context mContext;
    List<ScanResult> results;



    public WifiAdapter(Context context, List<ScanResult> scanResults){
        mContext=context;
        results=scanResults;
        layoutInflater=LayoutInflater.from(mContext);
    }
    @Override
    public MyWifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.device_single_row,parent,false);
        MyWifiViewHolder myWifiViewHolder=new MyWifiViewHolder(view);
        return myWifiViewHolder;
    }

    @Override
    public void onBindViewHolder(MyWifiViewHolder holder, int position) {
        String ssid=results.get(position).SSID;
        byte[] data1 = Base64.decode(ssid, Base64.DEFAULT);
        String text1 = null;
        try {
            text1 = new String(data1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String username=text1.replace("MyShareApp","");
        holder.txtSingleRowDevice.setText(username);
        holder.posn=position;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class MyWifiViewHolder extends RecyclerView.ViewHolder{

        TextView txtSingleRowDevice;
        int posn;
        public MyWifiViewHolder(View itemView) {
            super(itemView);
            txtSingleRowDevice=(TextView)itemView.findViewById(R.id.txtSingleRowDevice);
            txtSingleRowDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("chec",results.get(posn).SSID);
                    WifiConfiguration conf = new WifiConfiguration();
                    conf.SSID = "\"" + results.get(posn).SSID + "\"";
                    conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    WifiManager wifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
                    wifiManager.addNetwork(conf);
                    List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                    for( WifiConfiguration i : list ) {
                        if(i.SSID != null && i.SSID.equals("\"" + results.get(posn).SSID + "\"")) {
                            wifiManager.disconnect();
                            wifiManager.enableNetwork(i.networkId, true);
                            wifiManager.reconnect();
                            break;
                        }
                    }
                    mContext.startActivity(new Intent(mContext,FileDirectoryActivity.class));
                }
            });
        }
    }
}
