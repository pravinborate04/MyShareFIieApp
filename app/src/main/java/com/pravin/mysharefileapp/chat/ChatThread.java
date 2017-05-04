package com.pravin.mysharefileapp.chat;

import android.os.Messenger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Pravin Borate on 4/5/17.
 */

public class ChatThread extends Thread {

    private InetAddress receiverIP;
    private int port;
    private String stringToSend;
    private Messenger messenger;
    private Socket senderSocket;
    private int PKT_SIZE = 60*1024;


    private Socket socket = null;
    private InputStream inStream = null;
    private OutputStream outStream = null;
    public ChatThread(InetAddress receiverIP, int port, String stringToSend, Messenger messenger){
        this.receiverIP = receiverIP;
        this.port = port;
        this.messenger = messenger;
        this.stringToSend = stringToSend;
    }

    @Override
    public void run() {
        super.run();
        try
        {
            socket = new Socket(receiverIP,port);
            System.out.println("Connected");
            inStream = socket.getInputStream();
            outStream = socket.getOutputStream();

            byte[] readBuffer = new byte[200];
            int num = inStream.read(readBuffer);

            if (num > 0) {
                byte[] arrayBytes = new byte[num];
                System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
                String recvedMessage = new String(arrayBytes, "UTF-8");
                System.out.println("Received message :" + recvedMessage);
            }


            String typedMessage =stringToSend;
            if (typedMessage != null && typedMessage.length() > 0) {
                synchronized (socket) {
                    outStream.write(typedMessage.getBytes("UTF-8"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
