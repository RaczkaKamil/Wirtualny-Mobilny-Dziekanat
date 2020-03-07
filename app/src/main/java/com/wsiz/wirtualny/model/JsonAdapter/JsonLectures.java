package com.wsiz.wirtualny.model.JsonAdapter;

public class JsonLectures {
    private long przedmiotid;
    private String nazwa;

    public JsonLectures(long przedmiotid, String nazwa) {
        this.przedmiotid = przedmiotid;
        this.nazwa = nazwa;
    }

    public long getPrzedmiotid() {
        return przedmiotid;
    }

    public String getNazwa() {
        return nazwa;
    }
}
