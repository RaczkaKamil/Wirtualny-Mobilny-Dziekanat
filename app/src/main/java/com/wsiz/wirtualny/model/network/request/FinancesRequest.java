package com.wsiz.wirtualny.model.network.request;

import com.google.gson.annotations.SerializedName;

public class FinancesRequest {
    @SerializedName("api_key")
    String apiKey;

    @SerializedName("store_id")
    String store_id;


 //   @SerializedName("columns")
 //   ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(0, 1,2,3,4,5,6,7,8));

    public FinancesRequest(String apiKey, String store_id) {

        this.apiKey = apiKey;
        this.store_id = store_id;
    }
}
