package com.wsiz.wirtualny.model.db.RealmClasses;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    private long  id;

    @SerializedName("studentid")
    private long studentid;

    @SerializedName("album")
    private String album;

    @SerializedName("imie")
    private String imie;

    @SerializedName("nazwisko")
    private String nazwisko;

    @SerializedName("datarejestracji")
    private String datarejestracji;

    @SerializedName("active")
    private boolean active;

    @SerializedName("star")
    private boolean star;

    @SerializedName("finid")
    private long finid;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private long phone;

    @SerializedName("comment")
    private String comment;

    public void setId(long id) {
        this.id = id;
    }

    public void setStudentid(long studentid) {
        this.studentid = studentid;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setDatarejestracji(String datarejestracji) {
        this.datarejestracji = datarejestracji;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public void setFinid(long finid) {
        this.finid = finid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public long getStudentid() {
        return studentid;
    }

    public String getAlbum() {
        return album;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getDatarejestracji() {
        return datarejestracji;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isStar() {
        return star;
    }

    public long getFinid() {
        return finid;
    }

    public String getEmail() {
        return email;
    }

    public long getPhone() {
        return phone;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", studentid=" + studentid +
                ", album='" + album + '\'' +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", datarejestracji='" + datarejestracji + '\'' +
                ", active=" + active +
                ", star=" + star +
                ", finid=" + finid +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", comment='" + comment + '\'' +
                '}';
    }
}
