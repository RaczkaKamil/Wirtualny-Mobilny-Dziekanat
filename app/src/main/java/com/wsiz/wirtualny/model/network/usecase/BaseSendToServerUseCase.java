package com.wsiz.wirtualny.model.network.usecase;

 import retrofit2.adapter.rxjava.Result;
import rx.Observable;

public abstract class BaseSendToServerUseCase {
    public abstract Observable<Result<?>> execute();
}
