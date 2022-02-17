package com.wsiz.wirtualny.presenter.Finances;

import com.wsiz.wirtualny.model.db.RealmClasses.Finances;
import com.wsiz.wirtualny.model.network.manager.NetworkManager;
import com.wsiz.wirtualny.model.network.usecase.GetFinancesFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetGradesFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetLecturesFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetUserInfoFromServerUseCase;
import com.wsiz.wirtualny.presenter.Presenter;

import java.util.ArrayList;

import io.realm.RealmResults;

public class FinancesPresenter extends Presenter implements FinancesContract.Presenter {

    @Override
    public RealmResults<Finances> getFinances() {
         return getDatabaseDao().getFinancesFromBase();
    }

    @Override
    public void downlaodData() {
        GetFinancesFromServerUseCase getFinancesFromServerUseCase = new GetFinancesFromServerUseCase( NetworkManager.getInstance().getApiService());
        getFinancesFromServerUseCase.execute();
    }

}

