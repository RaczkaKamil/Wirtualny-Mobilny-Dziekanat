package com.wsiz.wd_mobile.JsonAdapter;

public class JsonGrade {
    long ocenaid;
    long semestrid;
    int studentid;
    long przedmiotid;
    long wykladowcaid;
    int terminid;
    int ocenatypid;
    String datamodyfikacji;

    public JsonGrade(long ocenaid, long semestrid, int studentid, long przedmiotid, long wykladowcaid, int terminid, int ocenatypid, String datamodyfikacji) {
        this.ocenaid = ocenaid;
        this.semestrid = semestrid;
        this.studentid = studentid;
        this.przedmiotid = przedmiotid;
        this.wykladowcaid = wykladowcaid;
        this.terminid = terminid;
        this.ocenatypid = ocenatypid;
        this.datamodyfikacji = datamodyfikacji;
    }

    public long getOcenaId() {
        return ocenaid;
    }

    public long getSemestrid() {
        return semestrid;
    }

    public int getStudentid() {
        return studentid;
    }

    public long getPrzedmiotid() {
        return przedmiotid;
    }

    public long getWykladowcaid() {
        return wykladowcaid;
    }

    public int getTerminid() {
        return terminid;
    }

    public int getOcenatypid() {
        return ocenatypid;
    }

    public String getDatamodyfikacji() {
        return datamodyfikacji;
    }
}
