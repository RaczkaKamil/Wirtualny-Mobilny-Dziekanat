package com.wsiz.wirtualny.model.db.RealmClasses;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Lectures extends RealmObject {
    @PrimaryKey
    private long  id;

    @SerializedName("przedmiotid")
    private long przedmiotid;

    @SerializedName("nazwa")
    private String nazwa;

    @SerializedName("active")
    private boolean active;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrzedmiotid() {
        return przedmiotid;
    }

    public void setPrzedmiotid(long przedmiotid) {
        this.przedmiotid = przedmiotid;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Lectures{" +
                "id=" + id +
                ", przedmiotid=" + przedmiotid +
                ", nazwa='" + nazwa + '\'' +
                ", active=" + active +
                '}';
    }
}
