package com.wsiz.wd_mobile.JsonAdapter;

public class JsonFinances {
    long txid;
    String date;
    String type;
    String details;
    long amount;
    long sourceAccount;
    long targetAccount;

    public JsonFinances(long txid, String date, String type, String details, long amount, long sourceAccount, long targetAccount) {
        this.txid = txid;
        this.date = date;
        this.type = type;
        this.details = details;
        this.amount = amount;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
    }

    public long getTxid() {
        return txid;
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

    public long getAmount() {
        return amount;
    }

    public long getSourceAccount() {
        return sourceAccount;
    }

    public long getTargetAccount() {
        return targetAccount;
    }
}
