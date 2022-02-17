package com.wsiz.wirtualny.model.network.usecase;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.db.RealmClasses.Lectures;
import com.wsiz.wirtualny.model.db.RealmManager.RealmManager;
import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.manager.Result;

import java.util.Map;

import io.realm.Realm;
import rx.Observable;

public class GetLecturesFromServerUseCase extends BaseSettingsUseCase {

    @NonNull
    private final Api api;
    private Gson gson = new Gson();


    public GetLecturesFromServerUseCase(@NonNull Api api) {
        this.api = api;
    }

    @Override
    protected Observable<Result<String>> execute(@NonNull Map<String, Object> params) {
        return api.getLectures(EasyPreferences.getToken())
                .doOnNext(getterResponse -> {
                    Realm realm = RealmManager.open();
                    realm.executeTransactionAsync(dataBase -> {
                        int id = 1;
                        Lectures[] data = gson.fromJson(getterResponse, Lectures[].class);
                        for (Lectures lectures : data) {
                            lectures.setId(id++);
                            dataBase.insertOrUpdate(lectures);
                        }
                    });
                })
                .map(Result::success);
    }
}
