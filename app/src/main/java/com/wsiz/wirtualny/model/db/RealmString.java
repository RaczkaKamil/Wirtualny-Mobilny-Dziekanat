package com.wsiz.wirtualny.model.db;

import io.realm.RealmObject;

/**
 * Created by Ruslan Kishai on 10/26/2017.
 * Copyright (C) 2017 EasyCount.
 */
public class RealmString extends RealmObject {
    private String value;

    public RealmString() {

    }

    public RealmString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
