package com.wsiz.wirtualny.model.db.RealmClasses;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Finances extends RealmObject {
    @PrimaryKey
    private long  id;

    @SerializedName("txid")
    private long txid;

    @SerializedName("date")
    private long date;

    @SerializedName("type")
    private String type;

    @SerializedName("details")
    private String details;

    @SerializedName("amount")
    private float amount;

    @SerializedName("sourceAccount")
    private long sourceAccount;

    @SerializedName("targetAccount")
    private long targetAccount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTxid() {
        return txid;
    }

    public void setTxid(long txid) {
        this.txid = txid;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(long sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public long getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(long targetAccount) {
        this.targetAccount = targetAccount;
    }

    @Override
    public String toString() {
        return "Finances{" +
                "id=" + id +
                ", txid=" + txid +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", details='" + details + '\'' +
                ", amount=" + amount +
                ", sourceAccount=" + sourceAccount +
                ", targetAccount=" + targetAccount +
                '}';
    }
}
