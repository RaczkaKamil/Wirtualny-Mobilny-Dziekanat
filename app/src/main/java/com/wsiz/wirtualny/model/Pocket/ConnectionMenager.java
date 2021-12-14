package com.wsiz.wirtualny.model.Pocket;

import android.content.Context;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.wsiz.wirtualny.model.JsonAdapter.JsonUserID;
import com.wsiz.wirtualny.model.retrofit.RetrofitClientInstance;
import com.wsiz.wirtualny.model.retrofit.RetrofitClientService;
import com.wsiz.wirtualny.model.retrofit.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnectionMenager {
    private String TAG = "ConnectionMenager";
    private boolean isError = false;
    private boolean isLoginCorrectError = false;
    public FileSaver fileSaver;
    private String TOKEN;
    private String STUDENT_ID;
    private String FINANCES_ID;
    private String LOGIN;
    private String ENCRYPTED_PASSWORD;
    private String PASSWORD;
Context ctx;
    private int errorCount = 0;

    private Snackbar bar;



    public ConnectionMenager(Context ctx,Snackbar bar) {
        Log.d(TAG,"Started ConnectionMenager");
        this.ctx = ctx;
        this.bar=bar;
        fileSaver = new FileSaver(ctx);
    }


    public ConnectionMenager(Context ctx) {
        Log.d(TAG,"Started ConnectionMenager");
        fileSaver = new FileSaver(ctx);
    }


    public void Login(String login,String encryptedPassword, String password)
    {
        Log.d(TAG,"Started Login");
        this.LOGIN = login;
        this.ENCRYPTED_PASSWORD = encryptedPassword;
        this.PASSWORD = password;
        connectLogin();
    }

    private void LocalUser(String TOKEN)
    {
        Log.d(TAG,"Started LocalUser");
     this.TOKEN=TOKEN;
     connectUser();
    }

    public void LocalNews(String TOKEN) {
        Log.d(TAG,"Started LocalNews");
        this.TOKEN = TOKEN;
        connectNews();
    }

    public void LocalGrade(String studentID) {
        Log.d(TAG,"Started LocalGrade");
        this.STUDENT_ID = studentID;
        connectGrade();
    }

    public void LocalLectures(String studentID) {
        Log.d(TAG,"Started LocalLectures");
        this.STUDENT_ID = studentID;
        connectLectures();
    }

    public void LocalFinances(String financesID) {
        Log.d(TAG,"Started Finances");
        this.FINANCES_ID = financesID;
        connectFinances();
    }

    private void connectLogin() {
        Thread thread = new Thread(() -> {

            /*
            RetrofitClientService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientService.class);
            Call<ResponseBody> call = service.logIn();
            call.enqueue(new Callback<ResponseBody>() {


                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                      String header = response.headers().get("Set-Cookie");

                    try {
                        String token = response.body().string();
                        EasyPreferences.setCookies(header,ctx);
                        EasyPreferences.setToken(token,ctx);




                            RetrofitClientService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientService.class);
                        HashMap<String, String> stringListHashMap = new HashMap<>();
                        stringListHashMap.put("album", LOGIN);
                        stringListHashMap.put("pass", ENCRYPTED_PASSWORD);
                        stringListHashMap.put("_token", token);
                            Call<ResponseBody> call2 = service.logIn2(stringListHashMap, "application/x-www-form-urlencoded");

                        call2.enqueue(new Callback<ResponseBody>() {

                                @Override
                                public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response2) {
                                    String header2 = response2.headers().get("Set-Cookie");
                                    try {
                                        String token2 = response2.body().string();
                                        System.out.println("-----");
                                        System.out.println("TOKEN: ");
                                        System.out.println(token2);
                                        System.out.println("Cookie: ");
                                        System.out.println(header2);
                                        System.out.println("-----");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    System.out.println("ERRR");
                                    System.out.println(t.getMessage());
                                }
                            });



                         fileSaver.saveToken(response.body().string());
                         fileSaver.saveLogin(LOGIN,PASSWORD);
                        LocalUser(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("err");
                }
            });

*/


            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/auth?album=" + LOGIN + "&pass=" + ENCRYPTED_PASSWORD);
                SSLContext  mDefaultSslContext = SSLContext.getInstance("TLS");
                mDefaultSslContext.init(null, null, null);
                SSLSocketFactory  mDefaultSslFactory = mDefaultSslContext.getSocketFactory();

                SSLContext sslcontext;
                SSLSocketFactory sslfactory;
                sslfactory = mDefaultSslFactory;

                // If using this factory, enable session resumption (abbreviated handshake)
                sslfactory = mDefaultSslContext.getSocketFactory();

                // If using this factory, enable full handshake each time
                sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, null, null);
                sslfactory = sslcontext.getSocketFactory();





                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setSSLSocketFactory(sslfactory);
                connection.connect();

                String cookie = connection.getHeaderField("Set-Cookie" );

                InputStream stream = connection.getInputStream();
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);
                    if (line.length() == 36) {
                        bar.setText("Zalogowano...");
                        fileSaver.saveToken(line);
                        fileSaver.saveLogin(LOGIN,PASSWORD);
                        String [] cookies = cookie.split(";");
                         EasyPreferences.setCookies("laravel_session=eyJpdiI6ImlJdnNlMkoxUnZFTzRaVTB3WVdZdXc9PSIsInZhbHVlIjoialc5YU9Pbkc3YWpLSEtWa1p1MFhibVRoaVZkb2JJTDdtdHc3eTY5YVBXNHVUeDdrR3ZQMHFGVGh1aG1zXC9EaGtpXC8zcWVXenlCeXJMV1JjWVwvUWNiblE9PSIsIm1hYyI6ImQ5N2U0YzJhYTM0ZjU5MWJjMWVlNTczZGZhMWQ3YmIwNDhlMTgwMGY4NTdlODUwNDA0MWQwMDAyMDQ2MWZiNWMifQ%3D%3D;",ctx);
                        LocalUser(line);
                        Log.d(TAG,"Connected Login");
                    } else {
                        errorCount++;
                        if (errorCount < 5) {
                            connectLogin();
                        }else{
                            isLoginCorrectError=true;
                        }
                    }
                }

            } catch (Exception e) {
                e.fillInStackTrace();
                Log.d(TAG,"Connected ERROR Login");
                errorCount++;
                if (errorCount < 5) {
                    connectLogin();
                }else{
                    isError=true;
                }
            }


        });
        thread.start();
    }

    private void connectUser() {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/user?wdauth=" +TOKEN);

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                InputStream stream = conn.getInputStream();
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);
                    Gson gson = new Gson();
                    JsonUserID jsonUserID = gson.fromJson(line, JsonUserID.class);
                    bar.setText("Ładowanie...");
                    fileSaver.saveUser(jsonUserID);
                    Log.d(TAG,"Connected to user");
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR user");
                errorCount++;
                if (errorCount < 5) {
                    connectUser();
                }else{
                    isError=true;
                }
            }
        });
        thread.start();
    }


    private void connectFinances() {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/fin/txs/" + FINANCES_ID + "?wdauth=" + TOKEN);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);

                InputStream stream = conn.getInputStream();
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);
                    fileSaver.saveFinances(line);
                    System.out.println("FINANCES: " + line);
                    Log.d(TAG,"Connected finances");
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR finances");
                errorCount++;
                if (errorCount < 5) {
                    connectFinances();
                }else{
                    isError=true;
                }

            }
        });
        thread.start();
    }

    private void connectLectures() {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/lectures?wdauth=" + TOKEN);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);

                InputStream stream = conn.getInputStream();
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "lectures> " + line);
                    Log.d(TAG,"Connected Lectures");
                    System.out.println("LECTURES: " + line);
                    fileSaver.saveLectures(line);
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR Lectures");
                errorCount++;
                if (errorCount < 5) {
                    connectLectures();
                }else{
                    isError=true;
                }

            }
        });
        thread.start();
    }

    private void connectGrade() {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/student/" + STUDENT_ID + "/notes?wdauth=" + TOKEN);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                InputStream stream = conn.getInputStream();
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "grade> " + line);
                    Log.d(TAG,"Connected Grade");
                    System.out.println("GRADE: " +line);
                    fileSaver.saveGrade(line);
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR Grade");
                errorCount++;
                if (errorCount < 5) {
                    connectGrade();
                }else{
                    isError=true;
                }

            }
        });
        thread.start();
    }

    private void connectNews() {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/news?wdauth=" + TOKEN);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                InputStream stream = conn.getInputStream();
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d(TAG,"Connected News");
                    fileSaver.saveNews(line);
                    System.out.println("NEWS: " +line);
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR News");
                errorCount++;
                if (errorCount < 5) {
                    connectNews();
                }else{
                    isError=true;
                }
            }
        });
        thread.start();
    }


    public boolean isError() {
        return isError;
    }

    public boolean isLoginCorrectError() {
        return isLoginCorrectError;
    }

    public void clearError(){
        isError = false;
        isLoginCorrectError=false;
        errorCount=0;
    }

    public boolean isAllComplete() {
        return fileSaver.isDownloadCompelted();
    }
}

