package com.wsiz.wirtualny.model.network.usecase;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.db.RealmClasses.User;
import com.wsiz.wirtualny.model.db.RealmManager.RealmManager;
import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.manager.Result;

import java.util.Map;

import io.realm.Realm;
 import rx.Observable;

public class GetUserInfoFromServerUseCase extends BaseSettingsUseCase {

    @NonNull
    private final Api api;
    private Gson gson = new Gson();


    public GetUserInfoFromServerUseCase(@NonNull Api api) {
        this.api = api;
    }

    @Override
    protected Observable<Result<String>> execute(@NonNull Map<String, Object> params) {
         return api.getUser(EasyPreferences.getToken())
                .doOnNext(getterResponse -> {
                     Realm realm = RealmManager.open();
                     realm.executeTransactionAsync(dataBase -> {
                            User data = gson.fromJson(getterResponse, User.class);
                            data.setId(1);
                            dataBase.insertOrUpdate(data);
                            EasyPreferences.setFinancesID(String.valueOf(data.getFinid()));
                            EasyPreferences.setStudentID(String.valueOf(data.getStudentid()));
                        });
                })
            .map(Result::success);
    }
}
