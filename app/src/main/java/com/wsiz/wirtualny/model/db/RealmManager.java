package com.wsiz.wirtualny.model.db;

import androidx.annotation.NonNull;

import io.realm.Realm;

/**
 * Created by Ruslan Kishai on 10/24/2017.
 * Copyright (C) 2017 EasyCount.
 */

public class RealmManager {

    private static Realm realm;

    public static Realm open() {
        realm = Realm.getDefaultInstance();
        return realm;
    }

    public static void close() {

    }



    public static DatabaseDao createDatabaseDao() {
        checkForOpenRealm();
        return new DatabaseDao(realm);
    }




    public static void clearAll(){
        checkForOpenRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.deleteAll();
            }
        });
    }





    private static void checkForOpenRealm() {
        if (realm == null || realm.isClosed())
            throw new IllegalStateException("RealmManager: Realm is closed, call open() method first");
    }
}
