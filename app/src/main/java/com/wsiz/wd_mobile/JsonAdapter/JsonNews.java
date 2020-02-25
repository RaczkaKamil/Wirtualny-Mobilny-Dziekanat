package com.wsiz.wd_mobile.JsonAdapter;

public class JsonNews {
    long ogloszenieid;
    String tytul;
    String tresc;
    long dataut;
    boolean active;
    String fileuuid;
    String filename;

    public JsonNews(long ogloszenieid, String tytul, String tresc, long dataut, boolean active, String fileuuid, String filename) {
        this.ogloszenieid = ogloszenieid;
        this.tytul = tytul;
        this.tresc = tresc;
        this.dataut = dataut;
        this.active = active;
        this.fileuuid = fileuuid;
        this.filename = filename;
    }

    public long getOgloszenieid() {
        return ogloszenieid;
    }

    public String getTresc() {
        return tresc;
    }

    public long getDataut() {
        return dataut;
    }

    public boolean isActive() {
        return active;
    }

    public String getFileuuid() {
        return fileuuid;
    }

    public String getFilename() {
        return filename;
    }

    public String getTytyl() {
        return tytul;
    }
}
