package com.wsiz.wirtualny.model.network.request;

import com.google.gson.annotations.SerializedName;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;

public class GetterRequest {
    @SerializedName("_token")
    String _token;
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;

    public GetterRequest(String _token, String username, String password) {
        this._token = _token;
        this.username = username;
        this.password = password;
    }
}
