package com.wsiz.wirtualny.model.network;


import com.wsiz.wirtualny.model.network.manager.HeaderRequest;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;



public interface  Api {

    @GET("get/wd-auth/auth")
    Observable<String> logIn(@Query("album") String album, @Query("pass") String pass );

    @POST("auth/login")
    Observable<Response<Void>> getHeader(@Body HeaderRequest headerRequest);


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
