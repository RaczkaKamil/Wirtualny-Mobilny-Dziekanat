package com.wsiz.wirtualny.model.network.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginResponse {

        @SerializedName("result")
        @Expose
        private String data;

    public String getData() {
        return data;
    }
}
