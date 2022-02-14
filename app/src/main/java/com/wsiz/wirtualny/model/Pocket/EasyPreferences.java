package com.wsiz.wirtualny.model.Pocket;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.wsiz.wirtualny.model.WSIZ_APP;

public class EasyPreferences {

    public static void setCookies(String cookies){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cookies", cookies);
        editor.apply();
    }

    public static String getCookies(){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        return preferences.getString("cookies", "");
    }

    public static void setToken(String cookies){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", cookies);
        editor.apply();
    }

    public static String getToken(){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
      String token = preferences.getString("token", "");
        return token;
    }

}
