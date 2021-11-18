package com.wsiz.wirtualny.model.retrofit;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;

public interface RetrofitClientService {

    @GET("get/wd-auth/auth?album=3718&pass=38dd815e66dbd0d47a2b876ca442e987")
    Call<ResponseBody> logIn();

    @GET("get/wd-auth/auth")
    Call<ResponseBody> logIn2(@QueryMap HashMap<String, String> stringListHashMap, @Header("Content-Type") String header);

    @GET("news/file/6668f9dc-633e-4aaa-893a-3ff179cd4a4f/zarzadzenie_Rektora_4listopada.pdf")
    Call<ResponseBody> getPDF(@Header("Cookie") String cookie);
}