package com.wsiz.wirtualny.Pocket;

import android.content.Context;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.wsiz.wirtualny.JsonAdapter.JsonUserID;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ConnectionMenager {
    private String TAG = "ConnectionMenager";
    public FileSaver fileSaver;
    private String TOKEN;
    private String STUDENT_ID;
    private String FINANCES_ID;
    private String LOGIN;
    private String ENCRYPTED_PASSWORD;
    private String PASSWORD;

    private int errorCount = 0;

    private Snackbar bar;



    public ConnectionMenager(Context ctx,Snackbar bar) {
        Log.d(TAG,"Started ConnectionMenager");
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
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/auth?album=" + LOGIN + "&pass=" + ENCRYPTED_PASSWORD);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
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
                        LocalUser(line);
                        Log.d(TAG,"Connected Login");
                    } else {
                        connectLogin();
                    }
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR Login");
                errorCount++;
                if (errorCount < 5) {
                    connectLogin();
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
                    Log.d(TAG,"Connected finances");
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR finances");
                errorCount++;
                if (errorCount < 5) {
                    connectFinances();
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
                    Log.d("Response: ", "> " + line);
                    Log.d(TAG,"Connected Lectures");
                    fileSaver.saveLectures(line);
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR Lectures");
                errorCount++;
                if (errorCount < 5) {
                    connectLectures();
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
                    Log.d("Response: ", "> " + line);
                    Log.d(TAG,"Connected Grade");
                    fileSaver.saveGrade(line);
                }

                conn.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR Grade");
                errorCount++;
                if (errorCount < 5) {
                    connectGrade();
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
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.d(TAG,"Connected ERROR News");
                errorCount++;
                if (errorCount < 5) {
                    connectNews();
                }
            }
        });
        thread.start();
    }




    public boolean isAllComplete() {
        return fileSaver.isDownloadCompelted();
    }
}

