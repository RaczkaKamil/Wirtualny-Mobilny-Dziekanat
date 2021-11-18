package com.wsiz.wirtualny.model.Pocket;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReader {

    private Context ctx;
    private String data;
    private String token;
    private int studentID;
    private int finID;
    private String userData;
    private String cookies;


    public void startReadToken(Context context) {
        ctx = context;
        TokenReader();
    }

    public void startReadUserID(Context context) {
        ctx = context;
        UserIDReader();
    }


    private void UserIDReader() {
        if (getUserFileID() < ctx.fileList().length) {
            try {
                FileInputStream fileInputStream;
                fileInputStream = ctx.openFileInput(ctx.fileList()[getUserFileID()]);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuffer = new StringBuilder();

                while ((data = bufferedReader.readLine()) != null) {
                    stringBuffer.append(data).append("\n");
                    String splited = stringBuffer.toString();
                    String[] splited2 = splited.split("/");
                    this.userData = splited;
                    this.studentID = Integer.valueOf(splited2[0]);
                    this.finID = Integer.valueOf(splited2[7]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getUserFileID() {
        int userFileNumber = ctx.fileList().length + 1;
        for (int i = 0; i < ctx.fileList().length; i++) {
            if (ctx.fileList()[i].contains("AccountInfo")) {
                userFileNumber = i;
                return userFileNumber;
            }

        }
        return userFileNumber;
    }


    private void TokenReader() {
        if (getTokenFileID() < ctx.fileList().length) {
            try {
                FileInputStream fileInputStream;
                fileInputStream = ctx.openFileInput(ctx.fileList()[getTokenFileID()]);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuffer = new StringBuilder();


                while ((data = bufferedReader.readLine()) != null) {
                    stringBuffer.append(data).append("\n");
                    this.token = stringBuffer.toString();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private int getTokenFileID() {
        int tokenFileNumber = ctx.fileList().length + 1;
        for (int i = 0; i < ctx.fileList().length; i++) {
            if (ctx.fileList()[i].contains("Token")) {
                tokenFileNumber = i;
                return tokenFileNumber;
            }
        }
        return tokenFileNumber;
    }


    public int getStudentid() {
        return this.studentID;
    }

    public int getFinid() {
        return this.finID;
    }

    public String getToken() {
        return this.token;
    }

    public String getUserData() {
        return this.userData;
    }


}
