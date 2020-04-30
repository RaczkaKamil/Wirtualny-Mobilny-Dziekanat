package com.wsiz.wirtualny.model.JsonAdapter;

public class JsonFinances {
    private String date;
    private String type;
    private String details;
    private  String amount;

    public JsonFinances(String date, String type, String details, String amount) {
        this.date = date;
        this.type = type;
        this.details = details;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }

    public String getAmount() {
        return amount;
    }

}
