package com.wsiz.wirtualny.model.db.RealmClasses;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class News extends RealmObject {
    @PrimaryKey
    private long  id;

    @SerializedName("ogloszenieid")
    private long ogloszenieid;

    @SerializedName("tytul")
    private String tytul;

    @SerializedName("tresc")
    private String tresc;

    @SerializedName("dataut")
    private long dataut;

    @SerializedName("active")
    private boolean active;

    @SerializedName("fileuuid")
    private String fileuuid;

    @SerializedName("filename")
    private String filename;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOgloszenieid() {
        return ogloszenieid;
    }

    public void setOgloszenieid(long ogloszenieid) {
        this.ogloszenieid = ogloszenieid;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

    public long getDataut() {
        return dataut;
    }

    public void setDataut(long dataut) {
        this.dataut = dataut;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFileuuid() {
        return fileuuid;
    }

    public void setFileuuid(String fileuuid) {
        this.fileuuid = fileuuid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", ogloszenieid=" + ogloszenieid +
                ", tytul='" + tytul + '\'' +
                ", tresc='" + tresc + '\'' +
                ", dataut='" + dataut + '\'' +
                ", active='" + active + '\'' +
                ", fileuuid='" + fileuuid + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
