package com.wsiz.wirtualny.model.Pocket;

import com.google.gson.annotations.SerializedName;

public class GradesCalculated {
    int id;
    private int przedmiotid;
    private String nazwaPrzedmiotu;
    private long terminid;
    private double ocenatypid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwaPrzedmiotu() {
        return nazwaPrzedmiotu;
    }

    public void setNazwaPrzedmiotu(String nazwaPrzedmiotu) {
        this.nazwaPrzedmiotu = nazwaPrzedmiotu;
    }

    public long getTerminid() {
        return terminid;
    }

    public void setTerminid(long terminid) {
        this.terminid = terminid;
    }

    public double getOcenatypid() {
        return ocenatypid;
    }

    public void setOcena (double ocenatypid) {
        this.ocenatypid = ocenatypid;
    }


    public int getPrzedmiotid() {
        return przedmiotid;
    }

    public void setPrzedmiotid(int przedmiotid) {
        this.przedmiotid = przedmiotid;
    }

    public void setOcenatypid(double ocenatypid) {
        this.ocenatypid = ocenatypid;
    }

    @Override
    public String toString() {
        return "GradesCalculated{" +
                "id=" + id +
                ", przedmiotid=" + przedmiotid +
                ", nazwaPrzedmiotu='" + nazwaPrzedmiotu + '\'' +
                ", terminid=" + terminid +
                ", ocenatypid=" + ocenatypid +
                '}';
    }
}
