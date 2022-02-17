package com.wsiz.wirtualny.presenter.Grade;

import com.wsiz.wirtualny.model.Pocket.GradesCalculated;
import com.wsiz.wirtualny.model.db.RealmClasses.Grades;
import com.wsiz.wirtualny.model.db.RealmClasses.Lectures;
import com.wsiz.wirtualny.model.network.manager.NetworkManager;
import com.wsiz.wirtualny.model.network.usecase.GetGradesFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetLecturesFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetUserInfoFromServerUseCase;
import com.wsiz.wirtualny.presenter.Presenter;

import java.util.ArrayList;

import io.realm.RealmResults;

public class GradesPresenter extends Presenter implements GradesContract.Presenter {

    @Override
    public ArrayList<GradesCalculated> getGrades(int semestr_id) {
        ArrayList<GradesCalculated> gradesArrayList = new ArrayList<>();
        RealmResults<Grades> gradesList = getDatabaseDao().getGradesFromBaseBySemesterID(semestr_id);
        RealmResults<Lectures> lecturesList = getDatabaseDao().getLecturesFromBase();
        for (Grades grades : gradesList) {
            GradesCalculated gradesCalculated = new GradesCalculated();
            gradesCalculated.setId((int) grades.getId());
            gradesCalculated.setOcena(getGradeFromType(grades.getOcenatypid()) );
            gradesCalculated.setPrzedmiotid((int) grades.getPrzedmiotid());
            gradesCalculated.setTerminid((int) grades.getTerminid());

            boolean isFound = false;
            for (Lectures lectures : lecturesList){
                if (lectures.getPrzedmiotid() == grades.getPrzedmiotid()){
                    isFound = true;
                    gradesCalculated.setNazwaPrzedmiotu(lectures.getNazwa());
                }
            }


            boolean exist = false;
            for (GradesCalculated gr: gradesArrayList) {
                if(gr.getPrzedmiotid() ==gradesCalculated.getPrzedmiotid()){
                    gr.setOcena(gradesCalculated.getOcenatypid());
                    gr.setTerminid(gradesCalculated.getTerminid());
                    exist = true;

                }
            }

            if(!exist && isFound)
            gradesArrayList.add(gradesCalculated);
        }
        return gradesArrayList;
    }

    @Override
    public RealmResults<Grades> getSemestesrList() {
        RealmResults<Grades> gradesList = getDatabaseDao().getSemesterFromBase();
        return gradesList;
    }

    @Override
    public void downlaodData() {
        GetLecturesFromServerUseCase getLecturesFromServerUseCase = new GetLecturesFromServerUseCase(NetworkManager.getInstance().getApiService());
        GetGradesFromServerUseCase getGradesFromServerUseCase = new GetGradesFromServerUseCase(NetworkManager.getInstance().getApiService());
        getLecturesFromServerUseCase.execute();
        getGradesFromServerUseCase.execute();
    }

    private double getGradeFromType(long notesIn){
        if (notesIn == 6) {
            return 5;
        } else if (notesIn == 5) {
            return 4.5;
        } else if (notesIn == 4) {
            return 4;
        } else if (notesIn == 3) {
            return 3.5;
        } else if (notesIn == 2) {
            return 3;
        } else if (notesIn == 1) {
            return 2;
        } else if (notesIn == 0) {
            return 0;
        }
        return 0;
    }
}

