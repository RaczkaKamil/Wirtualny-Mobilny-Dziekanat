package com.wsiz.wirtualny.model.Pocket;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.wsiz.wirtualny.model.db.RealmClasses.User;
import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.manager.NetworkManager;
import com.wsiz.wirtualny.model.network.manager.Result;
import com.wsiz.wirtualny.model.network.manager.HeaderRequest;
import com.wsiz.wirtualny.model.network.usecase.FetchFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetFinancesFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetNewsFromServerUseCase;
import com.wsiz.wirtualny.view.Activity.Login.LoginActivity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConnectionMenager {
    private String TAG = "ConnectionMenager";
     private LoginActivity loginActivity;
    private Api api =  NetworkManager.getInstance().getApiService();


    public void Login(String login, String encryptedPassword, String password, LoginActivity loginActivity) {
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
                            System.out.println("LOGIN OK");

                            api.getUser(res)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<String>() {
                                        @Override
                                        public void onCompleted() {
                                            System.out.println("USER COMPELET");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            System.out.println("USER ERROR");
                                            e.fillInStackTrace();
                                            loginActivity.showNetworkError();
                                        }

                                        @Override
                                        public void onNext(String s) {
                                            Gson gson = new Gson();
                                            User data = gson.fromJson(s, User.class);
                                            EasyPreferences.setFinancesID(String.valueOf(data.getFinid()));
                                            EasyPreferences.setStudentID(String.valueOf(data.getStudentid()));


                                            HeaderRequest request = new HeaderRequest(EasyPreferences.getToken(),EasyPreferences.getLogin(), EasyPreferences.getPassword());
                                            api.getHeader(request)
                                                    .subscribeOn(Schedulers.newThread())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(headers -> {
                                                        EasyPreferences.setCookies(headers.headers().get("Set-Cookie"));
                                                        System.out.println("SET COOKIES:");
                                                        System.out.println(headers.headers().get("Set-Cookie").toString());
                                                        loginActivity.delayLogin();
                                                    });
                                        }
                                    });



                        }else{
                            loginActivity.showError();
                        }
                     }

                });

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
                        loginActivity.delayLogin();
                    }
                });
    }


    public void  downloadFinances() {
        FetchFromServerUseCase.downloadFinances(api).execute()
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
                    }
                });
    }

    public void  downloadNews() {
        FetchFromServerUseCase.downloadNews(api).execute()
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
                    }
                });
    }


    public void  downloadGrades(){
        FetchFromServerUseCase.downloadGrades(api).execute()
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
                    }
                });
    }



}

