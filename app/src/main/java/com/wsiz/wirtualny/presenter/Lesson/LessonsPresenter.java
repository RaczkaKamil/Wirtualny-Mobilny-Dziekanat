package com.wsiz.wirtualny.presenter.Lesson;

import com.wsiz.wirtualny.model.network.manager.NetworkManager;
import com.wsiz.wirtualny.model.network.usecase.GetUserInfoFromServerUseCase;
import com.wsiz.wirtualny.presenter.Grade.GradesContract;
import com.wsiz.wirtualny.presenter.Presenter;

import java.util.ArrayList;

public class LessonsPresenter extends Presenter implements LessonsContract.Presenter {

    @Override
    public ArrayList<String> getLessons() {
         ArrayList<String> arrayList = new ArrayList<>();

         return arrayList;
    }

}

