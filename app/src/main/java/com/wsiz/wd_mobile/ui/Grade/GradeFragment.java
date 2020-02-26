package com.wsiz.wd_mobile.ui.Grade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.wsiz.wd_mobile.JsonAdapter.JsonGrade;
import com.wsiz.wd_mobile.JsonAdapter.JsonLectures;
import com.wsiz.wd_mobile.JsonAdapter.TranslatorOfGrades;
import com.wsiz.wd_mobile.ListAdapter.GradesListAdapter;
import com.wsiz.wd_mobile.MainActivity;
import com.wsiz.wd_mobile.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class GradeFragment extends Fragment {
    private JsonGrade[] jsonGrades;
    private JsonLectures[] jsonLectures;
    private ArrayList<String> semestrList = new ArrayList<>();
    private ArrayList<String> MessageslistOfString = new ArrayList<>();
    private GradesListAdapter gradesListAdapter;
    private TabLayout tabLayout;
    private boolean isLecturesDownloaded = false;
    private  boolean isGradeLoaded = false;
    private boolean isLecturesLoaded = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_grade, container, false);
        getGrade();

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("Semestr: ");
        tabLayout = root.findViewById(R.id.tabLayout);
        gradesListAdapter = new GradesListAdapter(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.list_grade);
        online_list.setAdapter(gradesListAdapter);
        online_list.setClickable(false);
        gradesListAdapter.notifyDataSetChanged();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab(Long.valueOf(semestrList.get(tab.getPosition())));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }

    private void selectedTab(long semestr) {
        TranslatorOfGrades translator = new TranslatorOfGrades(0);
        for (JsonGrade jsonGrade : jsonGrades) {
            if (jsonGrade.getSemestrid() == semestr) {
                translator.setNotesIn(jsonGrade.getOcenatypid());
            }
            setJsonLectures(jsonLectures, semestr);
        }
    }

    private void removeTab(int lastId, long lastSemestr) {
        for (int i = 0; i < 6; i++) {
            try {
                tabLayout.removeTab(Objects.requireNonNull(tabLayout.getTabAt(lastId)));
            } catch (NullPointerException e) {
                e.fillInStackTrace();
            }

        }
        Objects.requireNonNull(tabLayout.getTabAt(lastId - 1)).select();
        selectedTab(lastSemestr);
    }

    private void getGrade() {
        Thread thread = new Thread(() -> {
            try {
                MainActivity activity = (MainActivity) getActivity();
                assert activity != null;
                while (!activity.isSaved()) {
                    if (!isGradeLoaded) {
                        getAvailableGrade("ODCZYTANO STARE OCENY");
                        isGradeLoaded = true;
                    }

                }
                getAvailableGrade("ODCZYTANO NOWE OCENY");
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
                        getAvailableLectures("ODCZYTANO STARE OCENY2", semestr);
                        isLecturesLoaded = true;
                    }
                }
                getAvailableLectures("ODCZYTANO NOWE OCENY2", semestr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void getAvailableLectures(String message, long semestr) throws IOException {
        String data;
        FileInputStream fileInputStream;
        fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getLecturesFileNumber()]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuffer = new StringBuilder();


        while ((data = bufferedReader.readLine()) != null) {
            stringBuffer.append(data).append("\n");
            String splited = stringBuffer.toString();
            System.out.println("----------------------------------" + message + "------------------------");
            System.out.println(splited);
            Gson gson = new Gson();
            jsonLectures = gson.fromJson(splited, JsonLectures[].class);
            isLecturesDownloaded = true;
            setJsonLectures(jsonLectures, semestr);
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
            System.out.println("----------------------------------" + message + "------------------------");
            System.out.println(splited);
            Gson gson = new Gson();
            jsonGrades = gson.fromJson(splited, JsonGrade[].class);
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
                    semestrid = jsonGrade.getSemestrid();
                    semestrList.add(String.valueOf(semestrid));
                    count++;
                }
            }
            getLectures(semestrid);
            removeTab(count, semestrid);
        });
    }

    private void setJsonLectures(JsonLectures[] jsonLectures, long semestr) {
        if (isLecturesDownloaded) {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                MessageslistOfString.clear();
                TranslatorOfGrades translator = new TranslatorOfGrades(0);
                for (JsonGrade jsonGrade : jsonGrades) {
                    for (JsonLectures jsonLecture : jsonLectures) {
                        if (jsonGrade.getPrzedmiotid() == jsonLecture.getPrzedmiotid()) {
                            if (jsonGrade.getSemestrid() == semestr) {
                                double t0 = 0.0;
                                double t1 = 0.0;
                                double t2 = 0.0;
                                if (jsonGrade.getTerminid() == 1) {
                                    translator.setNotesIn(jsonGrade.getOcenatypid());
                                    t0 = translator.getNotesOut();
                                } else if (jsonGrade.getTerminid() == 2) {
                                    translator.setNotesIn(jsonGrade.getOcenatypid());
                                    t1 = translator.getNotesOut();
                                } else if (jsonGrade.getTerminid() == 3) {
                                    translator.setNotesIn(jsonGrade.getOcenatypid());
                                    t2 = translator.getNotesOut();
                                }

                                MessageslistOfString.add(jsonLecture.getNazwa() + "~~" + t0 + "~~" + t1 + "~~" + t2 + "~~" + "0.0");
                                gradesListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }


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