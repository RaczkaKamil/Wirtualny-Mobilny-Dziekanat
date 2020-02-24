package com.wsiz.wd_mobile.ui.Lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wsiz.wd_mobile.ListAdapter.FinancesListAdapter;
import com.wsiz.wd_mobile.ListAdapter.PersonelListAdapter;
import com.wsiz.wd_mobile.R;

import java.util.ArrayList;

public class LessonFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lesson, container, false);

        return root;
    }
}