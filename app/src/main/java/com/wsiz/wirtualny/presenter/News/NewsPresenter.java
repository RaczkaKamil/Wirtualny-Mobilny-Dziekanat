package com.wsiz.wirtualny.presenter.News;

import com.wsiz.wirtualny.model.db.RealmClasses.News;
import com.wsiz.wirtualny.model.network.manager.NetworkManager;
import com.wsiz.wirtualny.model.network.usecase.GetNewsFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetUserInfoFromServerUseCase;
import com.wsiz.wirtualny.presenter.Grade.GradesContract;
import com.wsiz.wirtualny.presenter.Presenter;

import java.util.ArrayList;

import io.realm.RealmResults;

public class NewsPresenter extends Presenter implements NewsContract.Presenter {

    @Override
    public RealmResults<News> getNews() {
        return getDatabaseDao().getNewsFromBase();
    }

    @Override
    public RealmResults<News> getNewsByName(String name) {
        return getDatabaseDao().getNewsFromBaseByName(name);
    }

    @Override
    public void downlaodData() {
        GetNewsFromServerUseCase getNewsFromServerUseCase = new GetNewsFromServerUseCase( NetworkManager.getInstance().getApiService());
        getNewsFromServerUseCase.execute();
    }
}

