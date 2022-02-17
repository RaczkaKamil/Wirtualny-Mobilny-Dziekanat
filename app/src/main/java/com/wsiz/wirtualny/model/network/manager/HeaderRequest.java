package com.wsiz.wirtualny.model.network.manager;

import com.google.gson.annotations.SerializedName;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;

public class HeaderRequest {
    @SerializedName("_token")
    String _token;
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;

    public HeaderRequest(String _token, String username, String password) {
        this._token = _token;
        this.username = username;
        this.password = password;
    }
}
