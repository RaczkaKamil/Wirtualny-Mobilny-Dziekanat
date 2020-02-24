package com.wsiz.wd_mobile.Pocket;

import android.content.Context;
import android.util.Log;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

public class LocalConnectionWriter {
    private Context ctx;
    private String TOKEN;
    private String STUDENT_ID;
    private String FINANCES_ID;
    boolean isNewsSaved = false;
    boolean isGradeSaved = false;
    boolean isFinancesSaved = false;
    boolean isLecturesSaved = false;

    public LocalConnectionWriter(Context ctx) {
        this.ctx = ctx;
    }

    public void LocalNews(String TOKEN) {
        this.TOKEN = TOKEN;
        connectNews();
    }

    public void LocalGrade(String studentID) {
        this.STUDENT_ID = studentID;
        connectGrade();
    }

    public void LocalLectures(String studentID) {
        this.STUDENT_ID = studentID;
        connectLectures();
    }
    public void LocalFinances(String financesID) {
        this.FINANCES_ID = financesID;
        connectFinances();
    }


    private void connectFinances(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/fin/txs/"+FINANCES_ID+"?wdauth=" +TOKEN);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    InputStream stream = conn.getInputStream();
                    BufferedReader reader = null;
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                        Log.d("Response: ", "> " + line);
                        saveFinances(line);
                    }

                    conn.disconnect();
                } catch (SSLHandshakeException e){
                    System.out.println("PROBLEM Z POLACZENIEM!");
                    connectFinances();
                } catch (Exception e) {
                    System.out.println("PROBLEM Z POLACZENIEM!");
                    connectNews();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void connectLectures(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/lectures?wdauth=" +TOKEN );
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    InputStream stream = conn.getInputStream();
                    BufferedReader reader = null;
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                        Log.d("Response: ", "> " + line);
                        saveLectures(line);
                    }

                    conn.disconnect();
                } catch (SSLHandshakeException e){
                    System.out.println("PROBLEM Z POLACZENIEM!");
                    connectFinances();
                } catch (Exception e) {
                    System.out.println("PROBLEM Z POLACZENIEM!");
                    connectNews();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    private void connectGrade(){
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/student/" +STUDENT_ID+"/notes?wdauth="+TOKEN);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);


                InputStream stream = conn.getInputStream();
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);
                   saveGrade(line);
                }

                conn.disconnect();
            } catch (SSLHandshakeException e){
                System.out.println("PROBLEM Z POLACZENIEM!");
                connectGrade();
            } catch (Exception e) {
                System.out.println("PROBLEM Z POLACZENIEM!");
                connectGrade();
                e.printStackTrace();
            }
        }
    });
    thread.start();
}

    private void connectNews()  {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/news?wdauth=" +TOKEN);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);


                    InputStream stream = conn.getInputStream();
                    BufferedReader reader = null;
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";


                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                        saveNews(line);
                    }

                    conn.disconnect();

                }catch (SSLHandshakeException e){
                    System.out.println("PROBLEM Z POLACZENIEM!");
                    connectNews();
                } catch (Exception e) {
                    System.out.println("PROBLEM Z POLACZENIEM!");
                    connectNews();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void saveNews(String newsJson) {
                        try {
                            FileOutputStream fileOutputStream = null;
                            fileOutputStream = ctx.openFileOutput("News", Context.MODE_PRIVATE);
                            fileOutputStream.write(newsJson.getBytes());
                            fileOutputStream.close();
                            System.out.println("-------------------ZAPISANO NEWSY-----------------");
                            System.out.println("News: " + newsJson);
                            this.isNewsSaved=true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

    private void saveLectures(String newsJson) {
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = ctx.openFileOutput("Lectures", Context.MODE_PRIVATE);
            fileOutputStream.write(newsJson.getBytes());
            fileOutputStream.close();
            System.out.println("-------------------ZAPISANO LECTURESY-----------------");
            System.out.println("Lectures: " + newsJson);
            this.isLecturesSaved=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private  void saveGrade(String gradeJson) {
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = ctx.openFileOutput("Grade", Context.MODE_PRIVATE);
            fileOutputStream.write(gradeJson.getBytes());
            fileOutputStream.close();
            System.out.println("-------------------ZAPISANO OCENY-----------------");
            System.out.println("Grade: " + gradeJson);
            this.isGradeSaved=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFinances(String financesJson){
    try {
        FileOutputStream fileOutputStream = null;
        fileOutputStream = ctx.openFileOutput("Finances", Context.MODE_PRIVATE);
        fileOutputStream.write(financesJson.getBytes());
        fileOutputStream.close();
        System.out.println("-------------------ZAPISANO FINANSE-----------------");
        System.out.println("Finanse: " + financesJson);
        this.isFinancesSaved=true;
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public boolean isAllComplete(){
        if(isGradeSaved&&isNewsSaved&&isFinancesSaved&&isLecturesSaved){
            return true;
        }else return false;
}
}

