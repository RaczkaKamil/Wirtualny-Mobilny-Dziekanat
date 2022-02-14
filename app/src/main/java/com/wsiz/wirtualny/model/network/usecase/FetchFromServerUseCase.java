package com.wsiz.wirtualny.model.network.usecase;

import androidx.annotation.NonNull;

import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.manager.Result;
import com.wsiz.wirtualny.model.network.response.GetterResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rx.Observable;


@SuppressWarnings("rawtypes")
public class FetchFromServerUseCase {
    @NonNull
    private final List<BaseSettingsUseCase> useCases;

    private FetchFromServerUseCase(@NonNull BaseSettingsUseCase... useCases) {

        this.useCases = Arrays.asList(useCases);
     }

    public Observable<Result<GetterResponse>> execute() {
        List<Observable<Result<GetterResponse>>> observables = new ArrayList<>(useCases.size());
        for (int i = 0; i < useCases.size(); i++) {

            observables.add(useCases.get(i).execute());
        }
        return Observable.zip(observables, args -> {
            for (Object arg : args) {
                if (arg instanceof Result.Error) {

                    Result.error(((Result.Error) arg).getError());
                }
            }
            return Result.success(new GetterResponse());
        });
    }

    public static FetchFromServerUseCase create(@NonNull Api api) {
        return new FetchFromServerUseCase(
                new GetLocationFromServerUseCase(api)

        );
    }

    public static FetchFromServerUseCase createWithProduct(@NonNull  Api api) {
        return new FetchFromServerUseCase(

                new GetLocationFromServerUseCase(api)
        );
    }
}
