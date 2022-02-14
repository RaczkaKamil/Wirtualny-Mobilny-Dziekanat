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
import com.wsiz.wirtualny.view.Activity.Report.BugReportActivity;
import com.wsiz.wirtualny.model.JsonAdapter.JsonGrade;
import com.wsiz.wirtualny.model.JsonAdapter.JsonLectures;
import com.wsiz.wirtualny.model.JsonAdapter.TranslatorOfGrades;
import com.wsiz.wirtualny.model.ListAdapter.GradesListAdapter;
import com.wsiz.wirtualny.view.Activity.Main.MainActivity;
import com.wsiz.wirtualny.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

    public class GradeFragment extends Fragment {
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


        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_grade, container, false);

            MainActivity activity = (MainActivity) getActivity();;
            activity.setToolbarVisible(false);

            tabLayout = root.findViewById(R.id.tabLayout);
            btn_bug = root.findViewById(R.id.btn_bug);
            btn_bug.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), BugReportActivity.class);
                intent.putExtra("bug", LogList);
                startActivity(intent);
            });


            final ListView online_list = root.findViewById(R.id.list_grade);
            gradecount1  = root.findViewById(R.id.gradecount_1);
            gradecount2  = root.findViewById(R.id.gradecount2);


            gradesListAdapter = new GradesListAdapter(MessageslistOfString, getContext());
            online_list.setAdapter(gradesListAdapter);
            online_list.setClickable(false);

            gradesListAdapter.notifyDataSetChanged();
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    try{
                        selectedTab(Long.valueOf(semestrList.get(tab.getPosition())));
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
            getGrade();
            return root;
    }

    private void selectedTab(long semestr) {
        LogList.add("\n"+"SlectedTab:");
        LogList.add("\n"+"Semestr: " + semestr);
        MessageslistOfString.clear();
        gradesListAdapter.notifyDataSetChanged();
        if(semestr==-1){
            MessageslistOfString.clear();
            gradesListAdapter.notifyDataSetChanged();
        }else{
            TranslatorOfGrades translator = new TranslatorOfGrades(0);
            for (JsonGrade jsonGrade : jsonGrades) {
                if (jsonGrade.getSemestrid()==semestr) {
                    translator.setNotesIn(jsonGrade.getOcenatypid());
                }
                setJsonLectures(jsonLectures, semestr);
            }
        }

    }

    private void removeTab(int lastId, long lastSemestr) {
        LogList.add("\n"+"RemovingTab: ");
        LogList.add("\n"+"Removing tab to: " + lastId);
        for (int i = 0; i < 8; i++) {
            try {
                tabLayout.removeTab(Objects.requireNonNull(tabLayout.getTabAt(lastId)));
                LogList.add("\n"+"Removing tab " + i);
            } catch (NullPointerException e) {
                LogList.add("\n"+"End removing tab at: "+i);
                e.fillInStackTrace();
            }

        }
        try{
            LogList.add("\n"+"Trying get tab at: " + (lastId - 1));
           Objects.requireNonNull(tabLayout.getTabAt(lastId - 1)).select();
            selectedTab(lastSemestr);
        }catch (NullPointerException e){
            e.fillInStackTrace();
        }

    }

    private void getGrade() {
        LogList.add("\n"+"Started getGrade:");
        Thread thread = new Thread(() -> {
            try {
                MainActivity activity = (MainActivity) getActivity();
                assert activity != null;
                while (!activity.isSaved()) {
                    if (!isGradeLoaded) {
                        getAvailableGrade("readed oldest grades");
                        isGradeLoaded = true;
                    }
                }
                getAvailableGrade("readed newest grades");
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }


    private void getLectures(long semestr) {
        Thread thread = new Thread(() -> {
            try {
                MainActivity activity = (MainActivity) getActivity();
                assert activity != null;
                while (!activity.isSaved()) {
                    if (!isLecturesLoaded) {
                        getAvailableLectures("readed oldest grades2", semestr);
                        isLecturesLoaded = true;
                    }
                }
                getAvailableLectures("readed newest grades2", semestr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void getAvailableLectures(String message, long semestr) throws IOException {
        try {


            String data;
            FileInputStream fileInputStream;
            fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getLecturesFileNumber()]);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();


            while ((data = bufferedReader.readLine()) != null) {
                stringBuffer.append(data).append("\n");
                String splited = stringBuffer.toString();
                Log.d(TAG, message);
                Gson gson = new Gson();
                jsonLectures = gson.fromJson(splited, JsonLectures[].class);
                isLecturesDownloaded = true;
                setJsonLectures(jsonLectures, semestr);
            }
        }catch (NullPointerException e){
            e.fillInStackTrace();
        }
    }


    private void getAvailableGrade(String message) throws IOException {
        String data;
        FileInputStream fileInputStream;
        fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getGradeFileNumber()]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuffer = new StringBuilder();


        while ((data = bufferedReader.readLine()) != null) {
            stringBuffer.append(data).append("\n");
            String splited = stringBuffer.toString();
            Log.d(TAG,message);
            Gson gson = new Gson();
            jsonGrades = gson.fromJson(splited, JsonGrade[].class);
            LogList.add("\n"+"getAvailableGrade:");
            LogList.add(splited);
            setJson(jsonGrades);
        }
    }

    private void setJson(JsonGrade[] jsonGrades) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            int count = 1;
            long semestrid = 0;

            for (JsonGrade jsonGrade : jsonGrades) {
              if (semestrid == 0) {
                    semestrid = jsonGrade.getSemestrid();
                    semestrList.add(String.valueOf(semestrid));
                }

                if (semestrid != jsonGrade.getSemestrid()) {
                    semestrid =jsonGrade.getSemestrid() ;
                    semestrList.add(String.valueOf(semestrid));
                    count++;
                }
            }
            LogList.add("\n"+"setJsonFromGrades: ");
            LogList.add("\n"+"semestrID: " + semestrid);
            LogList.add("\n"+"Count: " + count);
            LogList.add("\n"+"SemesterListSize: " + semestrList.size());
           getLectures(semestrid);
           removeTab(count, semestrid);
        });
    }

    private void setJsonLectures(JsonLectures[] jsonLectures, long semestr) {
        if (isLecturesDownloaded) {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                MessageslistOfString.clear();
                gradeLecturesList.clearList();
                gradesListAdapter.notifyDataSetChanged();
                TranslatorOfGrades translator = new TranslatorOfGrades(0);
                for (JsonGrade jsonGrade : jsonGrades) {
                    for (JsonLectures jsonLecture : jsonLectures) {
                        if (jsonGrade.getPrzedmiotid() == jsonLecture.getPrzedmiotid()) {
                            if (jsonGrade.getSemestrid() == semestr) {
                                double t0 = 0.0;
                                double t1 = 0.0;
                                double t2 = 0.0;
                                double t3 = 0.0;
                                if (jsonGrade.getTerminid() == 1) {
                                    translator.setNotesIn(jsonGrade.getOcenatypid());
                                    t0 = translator.getNotesOut();
                                } else if (jsonGrade.getTerminid() == 2) {
                                    translator.setNotesIn(jsonGrade.getOcenatypid());
                                    t1 = translator.getNotesOut();
                                } else if (jsonGrade.getTerminid() == 3) {
                                    translator.setNotesIn(jsonGrade.getOcenatypid());
                                    t2 = translator.getNotesOut();
                                } else if (jsonGrade.getTerminid() == 4) {
                                    translator.setNotesIn(jsonGrade.getOcenatypid());
                                    t3 = translator.getNotesOut();
                                }

                                GradeLecturesObiect gl = new GradeLecturesObiect(jsonLecture.getNazwa(),t0,t1,t2,t3);
                                gradeLecturesList.add(gl);

                            }
                        }
                    }
                }
                gradecount1.setText(gradeLecturesList.getGradeCunter()+"/"+gradeLecturesList.getLecturesList().size());
                gradecount2.setText(""+gradeLecturesList.getGradeAvg());
                for (int i = 0; i < gradeLecturesList.getLecturesList().size(); i++) {
                    MessageslistOfString.add(gradeLecturesList.getLecturesList().get(i).getNazwaPrzedmiotu()
                            +"~~"+gradeLecturesList.getLecturesList().get(i).getT0()
                            +"~~"+gradeLecturesList.getLecturesList().get(i).getT1()
                            +"~~"+gradeLecturesList.getLecturesList().get(i).getT2()
                            +"~~"+gradeLecturesList.getLecturesList().get(i).getT3());
                }


                gradesListAdapter.notifyDataSetChanged();
            });
        }

    }

    private int getGradeFileNumber() {
        for (int i = 0; i < Objects.requireNonNull(getContext()).fileList().length; i++) {
            if (getContext().fileList()[i].contains("Grade")) {
                return i;
            }
        }
        return -1;
    }

    private int getLecturesFileNumber() {
        for (int i = 0; i < Objects.requireNonNull(getContext()).fileList().length; i++) {
            if (getContext().fileList()[i].contains("Lectures")) {
                return i;
            }
        }
        return -1;
    }
}