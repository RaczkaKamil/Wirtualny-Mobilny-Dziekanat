package com.wsiz.wirtualny.model.db;

import androidx.annotation.NonNull;

import com.wsiz.wirtualny.model.db.RealmClasses.Finances;
import com.wsiz.wirtualny.model.db.RealmClasses.Grades;
import com.wsiz.wirtualny.model.db.RealmClasses.Lectures;
import com.wsiz.wirtualny.model.db.RealmClasses.News;
import com.wsiz.wirtualny.model.db.RealmClasses.User;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DatabaseDao {
    private final Realm realm;
    public DatabaseDao(@NonNull Realm realm) {
        this.realm = realm;
    }

    public User getUser(){
        return realm.where(User.class).findFirst();
    }


    public RealmResults<News> getNewsFromBase(){
        return realm.where(News.class).findAll();
    }

    public RealmResults<News> getNewsFromBaseWithFile(){
        return realm.where(News.class).contains("tytul", "Rozkład").notEqualTo("fileuuid", "null").findAllSorted("id", Sort.ASCENDING);
    }

    public RealmResults<News> getNewsFromBaseByName(String word){
        return realm.where(News.class).contains("tytul", word).or().contains("tresc", word).findAll();
    }
    public News getNewsFromBaseByID(int id){
         return realm.where(News.class).equalTo("id", id).findFirst();
    }
    public RealmResults<Grades> getGradesFromBaseBySemesterID(int semestrid){
        return realm.where(Grades.class).equalTo("semestrid", semestrid).findAll();
    }

    public RealmResults<Grades> getGradesFromBase(){
        return realm.where(Grades.class).findAll();
    }

    public RealmResults<Grades> getSemesterFromBase(){
        return realm.where(Grades.class).distinct("semestrid").sort("semestrid", Sort.ASCENDING) ;
    }

    public RealmResults<Lectures> getLecturesFromBase(){
        return realm.where(Lectures.class).findAll();
    }

    public RealmResults<Finances> getFinancesFromBase(){
        return realm.where(Finances.class).findAll();
    }
}
