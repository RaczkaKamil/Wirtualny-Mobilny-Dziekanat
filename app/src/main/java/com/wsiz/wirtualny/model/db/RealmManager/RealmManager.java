package com.wsiz.wirtualny.model.db.RealmManager;

import androidx.annotation.NonNull;

import com.wsiz.wirtualny.model.db.DatabaseDao;

import io.realm.Realm;



public class RealmManager {

    private static Realm realm;

    public static Realm open() {
        realm = Realm.getDefaultInstance();
        return realm;
    }

    public static void close() {

    }



    public static DatabaseDao createDatabaseDao() {
        open();
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
