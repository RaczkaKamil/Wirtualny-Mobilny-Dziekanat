package com.wsiz.wirtualny.model.Pocket;

import android.content.Context;
import android.util.Log;

import com.wsiz.wirtualny.model.JsonAdapter.JsonUserID;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileSaver {
    private String TAG = "FileSaver";
    private Context ctx;
    private Boolean isTokenSaved;
    private Boolean isAccoutSave;
    private Boolean isUserSave;
    private Boolean isFinancesSaved;
    private Boolean isLecturesSaved;
    private Boolean isGradeSaved;
    private Boolean isNewsSaved;
    private FileComparator fileComparator;


    FileSaver(Context ctx){
        this.ctx=ctx;
        isTokenSaved=false;
        isAccoutSave=false;
        isUserSave=false;
        isFinancesSaved=false;
        isLecturesSaved=false;
        isGradeSaved=false;
        isNewsSaved=false;

    }

    void saveNews(String newsJson) {
        try {
            fileComparator = new FileComparator(newsJson.length(),"News",ctx);
            FileOutputStream fileOutputStream;
            fileOutputStream = ctx.openFileOutput("News", Context.MODE_PRIVATE);
            fileOutputStream.write(newsJson.getBytes());
            fileOutputStream.close();
            Log.d(TAG,"News saved");
            this.isNewsSaved = true;
        } catch (IOException e) {
            Log.d(TAG,"News saved ERROR");
            e.printStackTrace();
        }
    }

    void saveLectures(String lecturesJson) {
        try {
            FileOutputStream fileOutputStream;
            fileOutputStream = ctx.openFileOutput("Lectures", Context.MODE_PRIVATE);
            fileOutputStream.write(lecturesJson.getBytes());
            fileOutputStream.close();
            Log.d(TAG,"Lectures saved");
            this.isLecturesSaved = true;
        } catch (IOException e) {
            Log.d(TAG,"Lectures saved ERROR");
            e.printStackTrace();
        }
    }


    void saveGrade(String gradeJson) {
        try {
            fileComparator = new FileComparator(gradeJson.length(),"Grade",ctx);
            FileOutputStream fileOutputStream;
            fileOutputStream = ctx.openFileOutput("Grade", Context.MODE_PRIVATE);
            fileOutputStream.write(gradeJson.getBytes());
            fileOutputStream.close();
            Log.d(TAG,"Grades saved");
            this.isGradeSaved = true;
        } catch (IOException e) {
            Log.d(TAG,"Grades saved ERROR");
            e.printStackTrace();
        }
    }

    void saveFinances(String financesJson) {
        try {
           fileComparator = new FileComparator(financesJson.length(),"Finances",ctx);
            FileOutputStream fileOutputStream;
            fileOutputStream = ctx.openFileOutput("Finances", Context.MODE_PRIVATE);
            fileOutputStream.write(financesJson.getBytes());
            fileOutputStream.close();
            Log.d(TAG,"Finances saved");
            this.isFinancesSaved = true;
        } catch (IOException e) {
            Log.d(TAG,"Finances saved ERROR");
            e.printStackTrace();
        }
    }

    void saveUser(JsonUserID jsonUserID) {
        try {
            FileOutputStream fileOutputStream;
            fileOutputStream = ctx.openFileOutput("AccountInfo", Context.MODE_PRIVATE);
            fileOutputStream.write(String.valueOf(jsonUserID.getStudentid()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.getAlbum()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.getImie()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.getNazwisko()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.getDataRejestracji()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.isActive()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.isStar()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.getFinid()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.getEmail()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.getPhone()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(String.valueOf(jsonUserID.getComment()).getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.close();
            Log.d(TAG,"User saved");
            this.isUserSave = true;
        } catch (IOException e) {
            Log.d(TAG,"User saved ERROR");
            e.printStackTrace();
        }
    }


    void saveToken(String token) {

        try {
            FileOutputStream fileOutputStream;
            fileOutputStream = ctx.openFileOutput("Token", Context.MODE_PRIVATE);
            fileOutputStream.write(token.getBytes());
            fileOutputStream.close();
            Log.d(TAG,"Token saved");
            this.isTokenSaved = true;
        } catch (IOException e) {
            Log.d(TAG,"Token saved ERROR");
            e.printStackTrace();
        }
    }

    void saveLogin(String login, String password) {
        try {
            FileOutputStream fileOutputStream;
            fileOutputStream = ctx.openFileOutput("AccountLogin", Context.MODE_PRIVATE);
            fileOutputStream.write(login.getBytes());
            fileOutputStream.write("/".getBytes());
            fileOutputStream.write(password.getBytes());
            fileOutputStream.close();
            Log.d(TAG,"Account saved");
            this.isAccoutSave = true;
        } catch (IOException e) {
            Log.d(TAG,"Account saved ERROR");
            e.printStackTrace();
        }
    }

    public Boolean isLoginComplete(){
        return isAccoutSave && isUserSave && isTokenSaved;
    }

    public Boolean isDownloadCompelted(){
        return isNewsSaved && isFinancesSaved && isLecturesSaved && isGradeSaved;
    }

}
