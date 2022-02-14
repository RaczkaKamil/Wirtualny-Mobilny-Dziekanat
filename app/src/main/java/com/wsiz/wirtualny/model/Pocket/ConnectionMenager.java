package com.wsiz.wirtualny.model.Pocket;

import android.content.Context;
import android.os.Handler;
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
        EasyPreferences.setToken(TOKEN);
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


            BufferedReader reader = null;
            HttpsURLConnection connection = null;
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/auth?album=" + LOGIN + "&pass=" + ENCRYPTED_PASSWORD);
                System.out.println(url.toString());



                connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);

                connection.connect();
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);
                    if (line.length() == 36) {
                        Handler handler = new Handler();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                bar.setText("Zalogowano...");

                            }
                        });
                        fileSaver.saveToken(line);
                        fileSaver.saveLogin(LOGIN,PASSWORD);
                        String cookie = connection.getHeaderField("Set-Cookie" );
                        String [] cookies = cookie.split(";");
                        EasyPreferences.setCookies("laravel_session=eyJpdiI6IlFCRCtcL09qR3hLU0VcL2FyY3Q5RHBDUT09IiwidmFsdWUiOiJ3NW1GbE05YW9lYlFGWDU5RFJRanRWelNGZVRSblZLWTN6N2V0MFBvamI3RmJmcklsZ1NhUVNiTjVMTFRybzNyOU5BVHpUcWdpdVVTV1wvaTlDbmUwOVE9PSIsIm1hYyI6IjkzMWRiODk5ZTQwNTYwOTJjN2JmMDA0ZTQxOWYzNTE1NWNkMjVjNTg5YzkwMTRjNzE2MWQ4YjJmNzVmYTEzZmEifQ");
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
            } catch (IOException e) {
                e.fillInStackTrace();
                e.printStackTrace();
            }








        });
        thread.start();
    }

    private void connectUser() {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/user?wdauth=" +TOKEN);
                System.out.println(url.toString());

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
                System.out.println(url.toString());

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
                System.out.println(url.toString());
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
                System.out.println(url.toString());
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
                System.out.println(url.toString());
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

