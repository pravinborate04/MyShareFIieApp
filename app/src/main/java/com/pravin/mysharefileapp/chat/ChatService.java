package com.pravin.mysharefileapp.chat;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;

import com.pravin.mysharefileapp.file_share.SenderThread;

import java.io.File;
import java.net.InetAddress;

/**
 * Created by Pravin Borate on 4/5/17.
 */

public class ChatService extends Service {

    private final String PORT = "PORT";
    private final String MESSENGER = "MESSENGER";
    private final String MESSAGE = "MESSAGE";
    private final String RECEIVER_IP = "RECEIVER_IP";

    private InetAddress receiverIP;
    private int port;
    private Messenger messenger;
    private String message;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle b = intent.getExtras();

        receiverIP = (InetAddress) b.get(RECEIVER_IP);
        port = (int) b.get(PORT);
        messenger = (Messenger) b.get(MESSENGER);
        message = (String) b.get(MESSAGE);


        ChatThread chatThread = new ChatThread(receiverIP,port,message,messenger);

        chatThread.start();

        return START_REDELIVER_INTENT;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
