package com.wsiz.wirtualny.model.network.manager;

import androidx.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.wsiz.wirtualny.BuildConfig;
import com.wsiz.wirtualny.model.WSIZ_APP;
import com.wsiz.wirtualny.model.db.RealmString;
import com.wsiz.wirtualny.model.network.Api;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;



import io.realm.RealmList;
import io.realm.RealmObject;
import okhttp3.ConnectionPool;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by Ruslan Kishai on 10/26/2017.
 * Copyright (C) 2017 EasyCount.
 */

public class NetworkManager {
     private static NetworkManager instance;
    private final Api apiService;
    private final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setLenient()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            })
            .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
                    }.getType(),
                    new RealmStringListTypeAdapter())
            .registerTypeAdapter(new TypeToken<Integer>() {
            }.getType(), new IntegerTypeAdapter())

            .create();

    private NetworkManager() {
        Retrofit.Builder jsonBuilder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
                apiService = createRetrofitService(Api.class, jsonBuilder);
    }

    private <S> S createRetrofitService(Class<S> serviceClass, Retrofit.Builder builder) {
        System.out.println("CREATE RETROFITE");
        ConnectionPool connectionPool = new ConnectionPool(10, 10000, TimeUnit.MINUTES);

        Retrofit retrofit = builder
                .client(httpClient
                        .cookieJar(new JavaNetCookieJar(new CookieManager()))
                        .pingInterval(2,TimeUnit.SECONDS)
                        .connectionPool(connectionPool)
                        .connectTimeout(35, TimeUnit.SECONDS)
                        .readTimeout(35, TimeUnit.SECONDS)
                        .writeTimeout(35, TimeUnit.SECONDS)

                        .addInterceptor(createLoggingInterceptor())
                        .build())
                .build();
        return retrofit.create(serviceClass);
    }

    @NonNull
    private Interceptor createLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public Api getApiService() {
        return instance.apiService;
    }

    private static class IntegerTypeAdapter extends TypeAdapter<Integer> {
        @Override
        public Integer read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            String stringValue = reader.nextString();
            try {
                return Integer.valueOf(stringValue);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public void write(JsonWriter writer, Integer value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    private static class RealmStringListTypeAdapter extends TypeAdapter<RealmList<RealmString>> {

        @Override
        public void write(JsonWriter out, RealmList<RealmString> value) {

        }

        @Override
        public RealmList<RealmString> read(JsonReader in) throws IOException {
            RealmList<RealmString> list = new RealmList<>();
            in.beginArray();
            while (in.hasNext()) {
                list.add(new RealmString(in.nextString()));
            }
            in.endArray();
            return list;
        }
    }

    private static class SessionCookieJar implements CookieJar {

        private List<Cookie> cookies;

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
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
