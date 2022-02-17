package com.wsiz.wirtualny.presenter.Finances;

import com.wsiz.wirtualny.model.db.RealmClasses.Finances;

import java.util.ArrayList;

import io.realm.RealmResults;

public interface FinancesContract {
    interface Presenter {
        RealmResults<Finances> getFinances();
        void downlaodData();
    }

}