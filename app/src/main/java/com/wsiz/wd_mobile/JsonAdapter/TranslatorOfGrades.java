package com.wsiz.wd_mobile.JsonAdapter;

public class TranslatorOfGrades {
    int notesIn;
    Double notesOut;

    public TranslatorOfGrades(int notesIn) {
        this.notesIn = notesIn;
    }

    public void setNotesIn(int notesIn) {
        this.notesIn = notesIn;
    }

    public double getNotesOut(){
        if(notesIn==6){
            return 5;
        }else if(notesIn ==5){
            return 4.5;
        }else if(notesIn==4){
            return 4;
        }else if(notesIn==3){
            return 3.5;
        }else if(notesIn==2){
            return 3;
        }else if(notesIn==1){
            return 2;
        }else if(notesIn==0){
            return 0;
        }
        return 0;
    }
}
