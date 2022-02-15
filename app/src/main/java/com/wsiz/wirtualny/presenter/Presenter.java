package com.wsiz.wirtualny.presenter;

import com.wsiz.wirtualny.model.db.DatabaseDao;
import com.wsiz.wirtualny.model.db.RealmManager;

public class Presenter {
    DatabaseDao databaseDao = RealmManager.createDatabaseDao();

    public DatabaseDao getDatabaseDao() {
        return databaseDao;
    }
}
