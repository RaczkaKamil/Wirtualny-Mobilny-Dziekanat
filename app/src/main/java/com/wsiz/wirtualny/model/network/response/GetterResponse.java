package com.wsiz.wirtualny.model.network.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetterResponse {

        @SerializedName("result")
        @Expose
        private ArrayList<JsonObject> data;

    @SerializedName("result_products")
    @Expose
    private ArrayList<JsonObject> data_products;

        @SerializedName("status")
        @Expose
        private int status;

    public ArrayList<JsonObject> getData_products() {
        return data_products;
    }

    public void setData_products(ArrayList<JsonObject> data_products) {
        this.data_products = data_products;
    }

    public ArrayList<JsonObject> getData() {
        return data;
    }

    public void setData(ArrayList<JsonObject> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
