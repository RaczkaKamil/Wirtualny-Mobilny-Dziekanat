package com.wsiz.wirtualny.model.retrofit;

import android.os.Build;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.ConnectionPool;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://dziekanat.wsi.edu.pl/";
    private String current_cookie = "";
    public static Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        ConnectionPool connectionPool = new ConnectionPool(10, 10, TimeUnit.MINUTES);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectionPool(connectionPool)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES);
        httpClient.interceptors().add(logging);

        httpClient.interceptors().add(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws
                    IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .header("Connection", "Keep-Alive")
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);

                if (!response.isSuccessful() || response.code()==503) {
                    connectionPool.evictAll();
                    return chain.proceed(request);
                } else {
                    // Customize or return the response
                    return response;
                }
            }
        });
        OkHttpClient client = httpClient.build();
        if (retrofit == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)

                        .client(
                                client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }
    private static class SessionCookieJar implements CookieJar {

        private List<Cookie> cookies;

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

            System.out.println("SAVE COOKIE: " + cookies.toString());
            if (url.encodedPath().endsWith("login")) {
                this.cookies = new ArrayList<>(cookies);
            }
        }


        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            if (!url.encodedPath().endsWith("login") && cookies != null) {
                return cookies;
            }
            return Collections.emptyList();
        }
    }
}
