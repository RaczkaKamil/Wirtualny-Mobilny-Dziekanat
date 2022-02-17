package com.wsiz.wirtualny.view.Grade;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.wsiz.wirtualny.model.JsonAdapter.GradeLecturesList;
import com.wsiz.wirtualny.model.JsonAdapter.GradeLecturesObiect;
import com.wsiz.wirtualny.model.Pocket.GradesCalculated;
import com.wsiz.wirtualny.model.db.RealmClasses.Grades;
import com.wsiz.wirtualny.model.db.RealmClasses.News;
import com.wsiz.wirtualny.presenter.Grade.GradesContract;
import com.wsiz.wirtualny.presenter.Grade.GradesPresenter;
import com.wsiz.wirtualny.presenter.News.NewsContract;
import com.wsiz.wirtualny.presenter.News.NewsPresenter;
import com.wsiz.wirtualny.view.Activity.Report.BugReportActivity;
import com.wsiz.wirtualny.model.JsonAdapter.JsonGrade;
import com.wsiz.wirtualny.model.JsonAdapter.JsonLectures;
 import com.wsiz.wirtualny.view.Activity.Main.MainActivity;
import com.wsiz.wirtualny.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import io.realm.RealmResults;

public class GradeFragment extends Fragment {
        private GradesContract.Presenter presenter;
        private ImageView btn_bug;
        private String TAG = "Grade fragment";
        private JsonGrade[] jsonGrades;
        private JsonLectures[] jsonLectures;
        private ArrayList<String> semestrList = new ArrayList<>();
        private ArrayList<String> MessageslistOfString = new ArrayList<>();
        private ArrayList<String> LogList = new ArrayList<>();
        private GradesListAdapter gradesListAdapter;
        private TabLayout tabLayout;
        private boolean isLecturesDownloaded = false;
        private  boolean isGradeLoaded = false;
        private boolean isLecturesLoaded = false;
        private GradeLecturesList gradeLecturesList = new GradeLecturesList();
        private TextView gradecount1;
        private TextView gradecount2;
        private ListView online_list;
    private ArrayList<GradesCalculated> grades;
    private RealmResults<Grades> semesters;


        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_grade, container, false);
            MainActivity activity = (MainActivity) getActivity();;
            activity.setToolbarVisible(false);
            presenter = new GradesPresenter();
            presenter.downlaodData();
            initView(root);
            return root;
    }

    private  void  initView(View root){
        tabLayout = root.findViewById(R.id.tabLayout);
        btn_bug = root.findViewById(R.id.btn_bug);
        online_list = root.findViewById(R.id.list_grade);
        gradecount1  = root.findViewById(R.id.gradecount_1);
        gradecount2  = root.findViewById(R.id.gradecount2);





        btn_bug.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), BugReportActivity.class);
            intent.putExtra("bug", LogList);
            startActivity(intent);
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try{
                    selectedTab(semesters.get(tab.getPosition()).getSemestrid());
                }catch (IndexOutOfBoundsException e){
                    selectedTab(Long.valueOf("-1"));
                    e.fillInStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        initSemesters();
    }

    private void initSemesters(){
        initList();
        semesters = presenter.getSemestesrList();
        try {
            removeTab(semesters.size(), semesters.get(semesters.size()-1).getSemestrid());
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    private void initList(){
        grades = presenter.getGrades(0);
        gradesListAdapter = new GradesListAdapter(grades, getContext());
        online_list.setAdapter(gradesListAdapter);
        online_list.setClickable(false);
        gradesListAdapter.notifyDataSetChanged();
    }


    private void selectedTab(long semestr) {
        grades = presenter.getGrades((int) semestr);
        gradesListAdapter = new GradesListAdapter(grades, getContext());
        online_list.setAdapter(gradesListAdapter);
        gradesListAdapter.notifyDataSetChanged();

        gradecount1.setText(gradesListAdapter.getNumberOfLessons());
        gradecount2.setText(""+gradesListAdapter.getAvg());

    }

    private void removeTab(int lastId, long lastSemestr) {
        for (int i = 0; i < 8; i++) {
            try {
                tabLayout.removeTab(Objects.requireNonNull(tabLayout.getTabAt(lastId)));
            } catch (NullPointerException e) {
                e.fillInStackTrace();
            }
        }
        Objects.requireNonNull(tabLayout.getTabAt(lastId - 1)).select();
        selectedTab(lastSemestr);
    }



}