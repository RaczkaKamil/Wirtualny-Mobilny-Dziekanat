package com.wsiz.wirtualny.presenter.News;

import com.wsiz.wirtualny.model.db.RealmClasses.News;

import java.util.ArrayList;

import io.realm.RealmResults;

public interface NewsContract {
    interface Presenter {
        RealmResults<News> getNews();
        RealmResults<News> getNewsByName(String name);
        void downlaodData();
    }

}