package com.wsiz.wirtualny.model.Pocket;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.wsiz.wirtualny.model.WSIZ_APP;

public class EasyPreferences {

    public static void setLogin(String login){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login", login);
        editor.apply();
    }

    public static String getLogin(){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        return preferences.getString("login", "");
    }

    public static void setPassword(String password){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public static String getPassword(){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        return preferences.getString("password", "");
    }

    public static void setEncryptedPassword(String password){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EncryptedPassword", password);
        editor.apply();
    }

    public static String getEncryptedPassword(){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        return preferences.getString("EncryptedPassword", "");
    }



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
    public static void setFinancesID(String id){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("financesID", id);
        editor.apply();
    }

    public static String getFinancesID(){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        String token = preferences.getString("financesID", "");
        return token;
    }

    public static void setStudentID(String id){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("studentID", id);
        editor.apply();
    }

    public static String getStudentID(){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(WSIZ_APP.getInstance());
        String token = preferences.getString("studentID", "");
        return token;
    }

}
