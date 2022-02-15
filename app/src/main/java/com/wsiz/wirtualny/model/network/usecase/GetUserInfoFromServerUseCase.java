package com.wsiz.wirtualny.model.network.usecase;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.db.RealmClasses.User;
import com.wsiz.wirtualny.model.db.RealmManager;
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
                     realm.executeTransactionAsync(realm1 -> {
                            User user = gson.fromJson(getterResponse, User.class);
                            user.setId(1);
                            realm.copyToRealmOrUpdate(user);
                            EasyPreferences.setFinancesID(String.valueOf(user.getFinid()));
                        });
                })
            .map(Result::success);
    }
}
