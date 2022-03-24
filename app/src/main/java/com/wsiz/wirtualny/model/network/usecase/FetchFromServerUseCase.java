package com.wsiz.wirtualny.model.network.usecase;
import androidx.annotation.NonNull;
import com.wsiz.wirtualny.model.network.Api;
import com.wsiz.wirtualny.model.network.manager.Result;
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

    public Observable<Result<String>> execute() {
        List<Observable<Result<String>>> observables = new ArrayList<>(useCases.size());
        for (int i = 0; i < useCases.size(); i++) {
            observables.add(useCases.get(i).execute());
        }
        return Observable.zip(observables, args -> {
            for (Object arg : args) {
                if (arg instanceof Result.Error) {
                    Result.error(((Result.Error) arg).getError());
                }
            }
            return Result.success("Success!");
        });
    }

    public static FetchFromServerUseCase downloadData(@NonNull Api api) {
        return new FetchFromServerUseCase(
                new GetUserInfoFromServerUseCase(api),
                new GetNewsFromServerUseCase(api),
                new GetFinancesFromServerUseCase(api),
                new GetGradesFromServerUseCase(api),
                new GetLecturesFromServerUseCase(api)
        );
    }

    public static FetchFromServerUseCase downloadFinances(@NonNull Api api) {
        return new FetchFromServerUseCase(
                new GetFinancesFromServerUseCase(api)
        );
    }

    public static FetchFromServerUseCase downloadNews(@NonNull Api api) {
        return new FetchFromServerUseCase(
                new GetNewsFromServerUseCase(api)
        );
    }

    public static FetchFromServerUseCase downloadGrades(@NonNull Api api) {
        return new FetchFromServerUseCase(
                new GetGradesFromServerUseCase(api),
                new GetLecturesFromServerUseCase(api)
        );
    }
}
