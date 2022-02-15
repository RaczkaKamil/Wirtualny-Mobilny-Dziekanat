package com.wsiz.wirtualny.model.network.usecase;

import androidx.annotation.NonNull;

import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.network.manager.Result;

import java.util.HashMap;
import java.util.Map;

 import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public abstract class BaseSettingsUseCase {
    protected static final String PARAM_OFFSET = "offset";
    protected static final String PARAM_API_KEY = "api_key";
    protected static final String PARAM_STORE_ID = "store_id";


    protected abstract Observable<Result<String>> execute(@NonNull Map<String, Object> params);

    public Observable<Result<String>> execute() {
        Map<String, Object> options = new HashMap<>();
        options.put(PARAM_OFFSET, 0);
        options.put(PARAM_API_KEY, EasyPreferences.getToken());
        options.put(PARAM_STORE_ID, EasyPreferences.getCookies());

        return execute(options)
                .flatMap(getterResponse -> {
                         return execute(options);
                })
                .subscribeOn(Schedulers.io())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                })
                .doOnError(
                        throwable -> Observable.just(Result.error(throwable)));
    }


}

