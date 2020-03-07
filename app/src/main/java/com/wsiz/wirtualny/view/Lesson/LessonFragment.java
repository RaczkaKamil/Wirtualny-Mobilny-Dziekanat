package com.wsiz.wirtualny.view.Lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wsiz.wirtualny.view.Main.MainActivity;
import com.wsiz.wirtualny.R;

public class LessonFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.setToolbarVisible(false);

        return inflater.inflate(R.layout.fragment_lesson, container, false);
    }
}