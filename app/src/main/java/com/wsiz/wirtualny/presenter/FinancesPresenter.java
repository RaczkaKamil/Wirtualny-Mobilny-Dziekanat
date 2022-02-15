package com.wsiz.wirtualny.presenter;

import com.wsiz.wirtualny.model.network.manager.NetworkManager;
import com.wsiz.wirtualny.model.network.usecase.GetUserInfoFromServerUseCase;

import java.util.ArrayList;

public class FinancesPresenter extends Presenter implements FinancesContract.Presenter{

    @Override
    public ArrayList<String> getFinances() {
         ArrayList<String> arrayList = new ArrayList<>();

        GetUserInfoFromServerUseCase getLocationFromServerUseCase = new GetUserInfoFromServerUseCase( NetworkManager.getInstance().getApiService());
        getLocationFromServerUseCase.execute();
         return arrayList;
    }

}

