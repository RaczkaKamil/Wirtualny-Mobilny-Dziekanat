package com.wsiz.wirtualny.model.network.usecase;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.db.RealmClasses.Grades;
import com.wsiz.wirtualny.model.db.RealmManager.RealmManager;
import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.NotificationManager;
import com.wsiz.wirtualny.model.network.manager.Result;

import java.util.Map;

import io.realm.Realm;
import rx.Observable;

public class GetGradesFromServerUseCase extends BaseSettingsUseCase {

    @NonNull
    private final Api api;
    private Gson gson = new Gson();


    public GetGradesFromServerUseCase(@NonNull Api api) {
        this.api = api;
    }

    @Override
    protected Observable<Result<String>> execute(@NonNull Map<String, Object> params) {
        return api.getGrades(Integer.parseInt(EasyPreferences.getStudentID()), EasyPreferences.getToken())
                .doOnNext(getterResponse -> {
                    Realm realm = RealmManager.open();
                    realm.executeTransactionAsync(dataBase -> {
                        int id = 1;
                        Grades[] data = gson.fromJson(getterResponse, Grades[].class);
                        if (RealmManager.createDatabaseDao().getGradesFromBase().size() != data.length)
                            new NotificationManager("Pojawiły się nowe oceny", "Oceny", "Wykryto zmiane w ocenach");
                        for (Grades grades : data) {
                            grades.setId(id++);
                            dataBase.insertOrUpdate(grades);
                        }
                    });
                })
                .map(Result::success);
    }
}
