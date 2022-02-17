package com.wsiz.wirtualny.presenter.Grade;

import com.wsiz.wirtualny.model.Pocket.GradesCalculated;
import com.wsiz.wirtualny.model.db.RealmClasses.Grades;

import java.util.ArrayList;

import io.realm.RealmResults;

public interface GradesContract {
    interface Presenter {
        ArrayList<GradesCalculated> getGrades(int semestr_id);
        RealmResults<Grades> getSemestesrList();
        void downlaodData();
    }

}