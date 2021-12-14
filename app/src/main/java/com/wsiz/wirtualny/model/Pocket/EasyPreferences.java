package com.wsiz.wirtualny.model.Pocket;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class EasyPreferences {

    public static void setCookies(String cookies, Context context){
         SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cookies", cookies);
        editor.apply();
    }

    public static String getCookies( Context context){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getString("cookies", "");
    }

    public static void setToken(String cookies, Context context){
         SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", cookies);
        editor.apply();
    }

    public static String getToken( Context context){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
      String token = preferences.getString("token", "");
        return token;
    }

}
