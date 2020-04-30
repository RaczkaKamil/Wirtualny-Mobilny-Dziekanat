package com.wsiz.wirtualny.model.JsonAdapter;

public class GradeLecturesObiect {
    String nazwaPrzedmiotu;

    public GradeLecturesObiect(String nazwaPrzedmiotu, double t0, double t1, double t2, double t3) {
        this.nazwaPrzedmiotu = nazwaPrzedmiotu;
        this.t0 = t0;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    double t0 ;
    double t1 ;
    double t2 ;
    double t3 ;

    public String getNazwaPrzedmiotu() {
        return nazwaPrzedmiotu;
    }

    public void setT0(double t0) {
        this.t0 = t0;
    }

    public void setT1(double t1) {
        this.t1 = t1;
    }

    public void setT2(double t2) {
        this.t2 = t2;
    }

    public void setT3(double t3) {
        this.t3 = t3;
    }

    public double getT0() {
        return t0;
    }

    public double getT1() {
        return t1;
    }

    public double getT2() {
        return t2;
    }

    public double getT3() {
        return t3;
    }
}
