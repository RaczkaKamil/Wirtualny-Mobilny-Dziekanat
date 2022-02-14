package com.wsiz.wirtualny.model.network;


import com.wsiz.wirtualny.model.network.request.FinancesRequest;
import com.wsiz.wirtualny.model.network.response.GetterResponse;


import retrofit2.http.Body;
import retrofit2.http.GET;
 import rx.Observable;

/**
 * Created by Ruslan Kishai on 10/26/2017.
 * Copyright (C) 2017 EasyCount.
 */

public interface  Api {


    @GET("api/api_v160/login/validate_login.php")
    Observable<GetterResponse> logIn(@Body FinancesRequest financesRequest);


}
