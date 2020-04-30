package com.wsiz.wirtualny.model.JsonAdapter;

public class JsonGrade {
    private long semestrid;
    private  long przedmiotid;
    private  int terminid;
    private  int ocenatypid;

    public JsonGrade(long semestrid, long przedmiotid, int terminid, int ocenatypid) {
        this.semestrid = semestrid;
        this.przedmiotid = przedmiotid;
        this.terminid = terminid;
        this.ocenatypid = ocenatypid;
    }

    public long getSemestrid() {
        return semestrid;
    }

    public long getPrzedmiotid() {
        return przedmiotid;
    }

    public int getTerminid() {
        return terminid;
    }

    public int getOcenatypid() {
        return ocenatypid;
    }

}