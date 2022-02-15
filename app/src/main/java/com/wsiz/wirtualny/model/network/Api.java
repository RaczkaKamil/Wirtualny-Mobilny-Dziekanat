package com.wsiz.wirtualny.model.network;


import com.wsiz.wirtualny.model.db.RealmClasses.User;
import com.wsiz.wirtualny.model.network.request.GetterRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ruslan Kishai on 10/26/2017.
 * Copyright (C) 2017 EasyCount.
 */

public interface  Api {

    @GET("get/wd-auth/auth")
    Observable<String> logIn(@Query("album") String album, @Query("pass") String pass );

    @POST("auth/login")
    Observable<Response<Void>> getHeader(@Body GetterRequest getterRequest  );


    @GET("get/wd-auth/user")
    Observable<String> getUser(@Query("wdauth") String wdauth);

    @GET("get/fin/txs/{finances_id}")
    Observable<String> getFinances(@Path("finances_id") int finances_id, @Query("wdauth") String wdauth);

    @GET("get/wd-news/student/{student_id}/notes")
    Observable<String> getGrades(@Path("student_id") int student_id, @Query("wdauth") String wdauth);

    @GET("get/wd-news/lectures")
    Observable<String> getLectures(@Query("wdauth") String wdauth);

    @GET("get/wd-news/news")
    Observable<String> getNews(@Query("wdauth") String wdauth);
}
