package com.wsiz.wirtualny.model.db;

import androidx.annotation.NonNull;

import io.realm.Realm;

public class DatabaseDao {
    private Realm realm;
    public DatabaseDao(@NonNull Realm realm) {
        this.realm = realm;
    }
}
