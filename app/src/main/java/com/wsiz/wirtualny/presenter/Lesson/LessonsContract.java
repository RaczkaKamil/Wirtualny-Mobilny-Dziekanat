package com.wsiz.wirtualny.presenter.Lesson;

import java.util.ArrayList;

public interface LessonsContract {
    interface Presenter {
        ArrayList<String> getLessons();
    }

}