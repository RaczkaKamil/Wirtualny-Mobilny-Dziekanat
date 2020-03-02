package com.wsiz.wirtualny.JsonAdapter;

public class JsonNews {
    private long ogloszenieid;
    private String tytul;
    private String tresc;
    private long dataut;
    private String fileuuid;
    private String filename;

    public JsonNews(long ogloszenieid, String tytul, String tresc, long dataut, String fileuuid, String filename) {
        this.ogloszenieid = ogloszenieid;
        this.tytul = tytul;
        this.tresc = tresc;
        this.dataut = dataut;
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
