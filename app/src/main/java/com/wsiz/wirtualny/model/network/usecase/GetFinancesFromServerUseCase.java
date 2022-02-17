package com.wsiz.wirtualny.model.network.usecase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.WSIZ_APP;
import com.wsiz.wirtualny.model.db.DatabaseDao;
import com.wsiz.wirtualny.model.db.RealmClasses.Finances;
import com.wsiz.wirtualny.model.db.RealmManager.RealmManager;
import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.NotificationManager;
import com.wsiz.wirtualny.model.network.manager.Result;
import com.wsiz.wirtualny.view.Activity.Main.MainActivity;

import java.util.Map;

import io.realm.Realm;
import rx.Observable;

public class GetFinancesFromServerUseCase extends BaseSettingsUseCase {

    @NonNull
    private final Api api;
    private Gson gson = new Gson();


    public GetFinancesFromServerUseCase(@NonNull Api api) {
        this.api = api;
    }

    @Override
    protected Observable<Result<String>> execute(@NonNull Map<String, Object> params) {
        return api.getFinances(Integer.parseInt(EasyPreferences.getFinancesID()), EasyPreferences.getToken())
                .doOnNext(getterResponse -> {
                    Realm realm = RealmManager.open();
                    realm.executeTransactionAsync(dataBase -> {
                        int id = 1;
                        Finances[] data = gson.fromJson(getterResponse, Finances[].class);
                        if (RealmManager.createDatabaseDao().getFinancesFromBase().size() != data.length)
                            new NotificationManager("Pojawiły się nowe finanse", "Finanse", "Wykryto zmiane w finansach");
                        for (Finances finances : data) {
                            finances.setId(id++);
                            dataBase.insertOrUpdate(finances);
                        }
                    });
                })
                .map(Result::success);
    }
}
