package com.wsiz.wirtualny.model.db.RealmClasses;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Grades extends RealmObject {
    @PrimaryKey
    private long  id;

    @SerializedName("ocenaid")
    private long ocenaid;

    @SerializedName("semestrid")
    private long semestrid;

    @SerializedName("studentid")
    private long studentid;

    @SerializedName("przedmiotid")
    private long przedmiotid;

    @SerializedName("wykladowcaid")
    private long wykladowcaid;

    @SerializedName("terminid")
    private long terminid;

    @SerializedName("ocenatypid")
    private long ocenatypid;

    @SerializedName("datamodyfikacji")
    private String datamodyfikacji;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOcenaid() {
        return ocenaid;
    }

    public void setOcenaid(long ocenaid) {
        this.ocenaid = ocenaid;
    }

    public long getSemestrid() {
        return semestrid;
    }

    public void setSemestrid(long semestrid) {
        this.semestrid = semestrid;
    }

    public long getStudentid() {
        return studentid;
    }

    public void setStudentid(long studentid) {
        this.studentid = studentid;
    }

    public long getPrzedmiotid() {
        return przedmiotid;
    }

    public void setPrzedmiotid(long przedmiotid) {
        this.przedmiotid = przedmiotid;
    }

    public long getWykladowcaid() {
        return wykladowcaid;
    }

    public void setWykladowcaid(long wykladowcaid) {
        this.wykladowcaid = wykladowcaid;
    }

    public long getTerminid() {
        return terminid;
    }

    public void setTerminid(long terminid) {
        this.terminid = terminid;
    }

    public long getOcenatypid() {
        return ocenatypid;
    }

    public void setOcenatypid(long ocenatypid) {
        this.ocenatypid = ocenatypid;
    }

    public String getDatamodyfikacji() {
        return datamodyfikacji;
    }

    public void setDatamodyfikacji(String datamodyfikacji) {
        this.datamodyfikacji = datamodyfikacji;
    }

    @Override
    public String toString() {
        return "Grades{" +
                "id=" + id +
                ", ocenaid=" + ocenaid +
                ", semestrid=" + semestrid +
                ", studentid=" + studentid +
                ", przedmiotid=" + przedmiotid +
                ", wykladowcaid=" + wykladowcaid +
                ", terminid=" + terminid +
                ", ocenatypid=" + ocenatypid +
                ", datamodyfikacji='" + datamodyfikacji + '\'' +
                '}';
    }
}
