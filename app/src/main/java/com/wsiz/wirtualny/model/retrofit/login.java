package com.wsiz.wirtualny.model.retrofit;

import com.google.gson.annotations.SerializedName;

public class login {
    @SerializedName("")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
