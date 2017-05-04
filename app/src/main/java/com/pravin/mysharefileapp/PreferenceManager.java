package com.pravin.mysharefileapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;


/**
 * Created by Saurabh Singh on 9/8/16.
 */

public class PreferenceManager {

    private static final String SHARED_PREF = "shareapp";

    public static final String USER_NAME = "userName";

    public static final String TEMP_PORT = "tempPort";

    private static PreferenceManager preferenceManager;
    private static SharedPreferences preferences;


    public static PreferenceManager getInstance() {

        if (preferenceManager == null || preferences == null) {
            preferenceManager = new PreferenceManager();
            preferences = App.get().getSharedPreferences(SHARED_PREF, Context.MODE_APPEND);
        }

        return preferenceManager;
    }


    public void saveStringValue(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        editor.apply();

    }

    public void setUserDetail() {

    }

    public void saveIntegerValue(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
        editor.apply();
    }

    public int getIntValue(String key) {
        return preferences.getInt(key, 0);
    }

    public String getStringValue(String key) {
        return preferences.getString(key, "");
    }

    public void resetAccount() {

        preferences.edit().clear().apply();

    }

    public boolean getBoolValue(String key) {
        return preferences.getBoolean(key, false);
    }

    public void saveBoolValue(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
        editor.apply();
    }
    public void setBasicAuthString(String key, String contactID, String BasicAuthUid){
        String basicAuthHeaderString;
        basicAuthHeaderString= contactID+"_"+"NZL" + ":" +BasicAuthUid;

        Log.e("BasicAuthHeaderString",basicAuthHeaderString);
        final String basic =
                "Basic " + Base64.encodeToString(basicAuthHeaderString.getBytes(), Base64.NO_WRAP);
        Log.e("Basic",basic);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, basic);
        editor.commit();
        editor.apply();

    }

}
