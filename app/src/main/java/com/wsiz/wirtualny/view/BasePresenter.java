package com.wsiz.wirtualny.view;

import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.manager.NetworkManager;

public abstract class BasePresenter {
    protected Api api;

    public BasePresenter() {
        api = NetworkManager.getInstance().getApiService();
    }
}
