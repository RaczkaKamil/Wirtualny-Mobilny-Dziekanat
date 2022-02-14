package com.wsiz.wirtualny.model;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WSIZ_APP  extends Application {


        private static WSIZ_APP instance;

        public synchronized static WSIZ_APP getInstance() {
            if (instance == null)
                instance = new WSIZ_APP();
            return instance;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            instance = this;


            Realm.init(instance);

            final RealmConfiguration configuration = new RealmConfiguration.Builder().schemaVersion(1).deleteRealmIfMigrationNeeded().build();

            Realm.setDefaultConfiguration(configuration);
            Realm.getInstance(configuration);


        }
}
