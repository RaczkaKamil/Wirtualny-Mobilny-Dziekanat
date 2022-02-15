package com.wsiz.wirtualny.model.Pocket;

import android.content.Context;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.manager.NetworkManager;
import com.wsiz.wirtualny.model.network.manager.Result;
import com.wsiz.wirtualny.model.network.request.GetterRequest;
import com.wsiz.wirtualny.model.network.usecase.FetchFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetUserInfoFromServerUseCase;
import com.wsiz.wirtualny.view.Activity.Login.LoginActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import retrofit2.Response;
import retrofit2.http.Headers;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private  LoginActivity loginActivity;
    private int errorCount = 0;

 Api api =  NetworkManager.getInstance().getApiService();


    public ConnectionMenager(Context ctx,Snackbar bar) {
        Log.d(TAG,"Started ConnectionMenager");
         fileSaver = new FileSaver(ctx);
    }


    public ConnectionMenager(Context ctx) {
        Log.d(TAG,"Started ConnectionMenager");
        fileSaver = new FileSaver(ctx);
    }



    public void Login(String login, String encryptedPassword, String password, LoginActivity loginActivity)
    {
        this.loginActivity = loginActivity;
        Log.d(TAG,"Started Login");
        connectLogin(login, password,encryptedPassword);
    }


    private void connectLogin(String login,String password, String encryptedPassword) {
        api.logIn(login,encryptedPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        EasyPreferences.setLogin(login);
                        EasyPreferences.setPassword(password);
                        EasyPreferences.setEncryptedPassword(encryptedPassword);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"Login Error");
                        e.printStackTrace();
                        loginActivity.showNetworkError();
                     }

                    @Override
                    public void onNext(String res) {
                        if(!res.contains("FAILED")){
                            EasyPreferences.setToken(res);
                            getUserInfo();

                            GetterRequest request = new GetterRequest(EasyPreferences.getToken(),EasyPreferences.getLogin(), EasyPreferences.getPassword());
                            api.getHeader(request)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(headers -> {

                                        System.out.println(headers.headers().get("Set-Cookie").toString());
                                        loginActivity.delayLogin();
                                    });

                        }else{
                            loginActivity.showError();
                        }
                     }

                });

    }

    public void getUserInfo(){
        new GetUserInfoFromServerUseCase(api).execute();
    }

    public void downloadData(){
        FetchFromServerUseCase.downloadData(api).execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<String>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "refreshDataFromServer: FINISHED");
                     }

                    @Override
                    public void onError(Throwable e) {
e.printStackTrace();
                    }

                    @Override
                    public void onNext(Result<String> result) {

                        System.out.println(result);
                    }
                });


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

