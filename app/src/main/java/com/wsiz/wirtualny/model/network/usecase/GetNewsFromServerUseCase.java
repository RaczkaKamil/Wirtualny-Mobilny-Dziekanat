package com.wsiz.wirtualny.model.network.usecase;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.db.RealmClasses.News;
import com.wsiz.wirtualny.model.db.RealmManager.RealmManager;
import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.NotificationManager;
import com.wsiz.wirtualny.model.network.manager.Result;

import java.util.Map;

import io.realm.Realm;
import rx.Observable;

public class GetNewsFromServerUseCase extends BaseSettingsUseCase {

    @NonNull
    private final Api api;
    private Gson gson = new Gson();


    public GetNewsFromServerUseCase(@NonNull Api api) {
        this.api = api;
    }

    @Override
    protected Observable<Result<String>> execute(@NonNull Map<String, Object> params) {
        return api.getNews(EasyPreferences.getToken())
                .doOnNext(getterResponse -> {
                    Realm realm = RealmManager.open();
                    realm.executeTransactionAsync(dataBase -> {
                        int id = 1;
                        News[] data = gson.fromJson(getterResponse, News[].class);
                        if (RealmManager.createDatabaseDao().getNewsFromBase().size() != data.length)
                            new NotificationManager("Pojawiły się nowe aktualności", "Aktualności", "Wykryto zmiane w aktualnościach");

                        for (News news : data) {
                            news.setId(id++);
                            dataBase.insertOrUpdate(news);
                        }
                    });
                })
                .map(Result::success);
    }
}
