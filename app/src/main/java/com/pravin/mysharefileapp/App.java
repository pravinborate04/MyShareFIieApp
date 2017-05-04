package com.pravin.mysharefileapp;

import android.app.Application;


public class App extends Application {

    public static App instance;


    public static final App get()
    {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
