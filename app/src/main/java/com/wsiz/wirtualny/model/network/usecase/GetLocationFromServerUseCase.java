package com.wsiz.wirtualny.model.network.usecase;

import android.location.LocationRequest;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.db.RealmManager;
import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.manager.Result;
import com.wsiz.wirtualny.model.network.request.FinancesRequest;
import com.wsiz.wirtualny.model.network.response.GetterResponse;

import java.util.ArrayList;
import java.util.Map;

import io.realm.Realm;
 import rx.Observable;

public class GetLocationFromServerUseCase extends BaseSettingsUseCase {

    @NonNull
    private final Api api;

    public GetLocationFromServerUseCase(@NonNull Api api) {
        this.api = api;
    }

    @Override
    protected  Observable<Result<GetterResponse>> execute(@NonNull Map<String, Object> params) {
        FinancesRequest request = new FinancesRequest(EasyPreferences.getCookies(), EasyPreferences.getCookies());
        return api.logIn(request)
                .doOnNext(getterResponse -> {
                        Realm realm = RealmManager.open();
                        ArrayList<JsonObject> jsonObjects = getterResponse.getData();
                        realm.executeTransactionAsync(realm1 -> {
                            for (JsonObject obiect :
                                    jsonObjects) {



                            }

                           }
                        );


                })
            .map(Result::success);
    }
}
