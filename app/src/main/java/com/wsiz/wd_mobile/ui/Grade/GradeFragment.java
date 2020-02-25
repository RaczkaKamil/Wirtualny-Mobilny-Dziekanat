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
    JsonGrade[] jsonGrades;
    JsonLectures[] jsonLectures;
    ArrayList<String> semestrList = new ArrayList<String>();
    ArrayList<String> MessageslistOfString = new ArrayList<String>();
    GradesListAdapter gradesListAdapter;
    TabLayout tabLayout;
    boolean isLecturesDownloaded = false;
    boolean isGradeLoaded = false;
    boolean isLecturesLoaded = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_grade, container, false);
        getGrade();

        ((MainActivity) getActivity()).setActionBarTitle("Semestr: ");
        tabLayout = root.findViewById(R.id.tabLayout);
        gradesListAdapter = new GradesListAdapter(MessageslistOfString, getContext());
        final ListView online_list = root.findViewById(R.id.list_grade);
        online_list.setAdapter(gradesListAdapter);
        online_list.setClickable(false);
        gradesListAdapter.notifyDataSetChanged();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab(tab.getPosition(), Long.valueOf(semestrList.get(tab.getPosition())));
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

    public void selectedTab(int tab, long semestr) {
        int count = 0;
        TranslatorOfGrades translator = new TranslatorOfGrades(0);
        for (int i = 0; i < jsonGrades.length; i++) {
            if (jsonGrades[i].getSemestrid() == semestr) {
                translator.setNotesIn(jsonGrades[i].getOcenatypid());
                count++;
            }
            setJsonLectures(jsonLectures, semestr);
        }
    }

    public void removeTab(int lastId, long lastSemestr) {
        for (int i = 0; i < 6; i++) {
            try {
                tabLayout.removeTab(tabLayout.getTabAt(lastId));
            } catch (NullPointerException e) {
                e.fillInStackTrace();
            }

        }
        tabLayout.getTabAt(lastId - 1).select();
        selectedTab(lastId - 1, lastSemestr);
    }

    private void getGrade() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MainActivity activity = (MainActivity) getActivity();
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
            }
        });

        thread.start();
    }


    private void getLectures(long semestr) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MainActivity activity = (MainActivity) getActivity();
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
            }
        });
        thread.start();
    }

    private void getAvailableLectures(String message, long semestr) throws IOException {
        String data;
        FileInputStream fileInputStream = null;
        fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getLecturesFileNumber()]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();


        while ((data = bufferedReader.readLine()) != null) {
            stringBuffer.append(data + "\n");
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
        FileInputStream fileInputStream = null;
        fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getGradeFileNumber()]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();


        while ((data = bufferedReader.readLine()) != null) {
            stringBuffer.append(data + "\n");
            String splited = stringBuffer.toString();
            System.out.println("----------------------------------" + message + "------------------------");
            System.out.println(splited);
            Gson gson = new Gson();
            jsonGrades = gson.fromJson(splited, JsonGrade[].class);
            setJson(jsonGrades);
        }
    }

    public void setJson(JsonGrade[] jsonGrades) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                int count = 1;
                long semestrid = 0;

                for (int i = 0; i < jsonGrades.length; i++) {
                    if (semestrid == 0) {
                        semestrid = jsonGrades[i].getSemestrid();
                        semestrList.add(String.valueOf(semestrid));
                    }

                    if (semestrid != jsonGrades[i].getSemestrid()) {
                        semestrid = jsonGrades[i].getSemestrid();
                        semestrList.add(String.valueOf(semestrid));
                        count++;
                    }
                }
                getLectures(semestrid);
                removeTab(count, semestrid);
            }
        });
    }

    public void setJsonLectures(JsonLectures[] jsonLectures, long semestr) {
        if (isLecturesDownloaded) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    MessageslistOfString.clear();
                    TranslatorOfGrades translator = new TranslatorOfGrades(0);
                    for (int i = 0; i < jsonGrades.length; i++) {
                        for (int j = 0; j < jsonLectures.length; j++) {
                            if (jsonGrades[i].getPrzedmiotid() == jsonLectures[j].getPrzedmiotid()) {
                                if (jsonGrades[i].getSemestrid() == semestr) {
                                    double t0 = 0.0;
                                    double t1 = 0.0;
                                    double t2 = 0.0;
                                    if (jsonGrades[i].getTerminid() == 1) {
                                        translator.setNotesIn(jsonGrades[i].getOcenatypid());
                                        t0 = translator.getNotesOut();
                                    } else if (jsonGrades[i].getTerminid() == 2) {
                                        translator.setNotesIn(jsonGrades[i].getOcenatypid());
                                        t1 = translator.getNotesOut();
                                    } else if (jsonGrades[i].getTerminid() == 3) {
                                        translator.setNotesIn(jsonGrades[i].getOcenatypid());
                                        t2 = translator.getNotesOut();
                                    }

                                    MessageslistOfString.add(jsonLectures[j].getNazwa() + "~~" + t0 + "~~" + t1 + "~~" + t2 + "~~" + "0.0");
                                    gradesListAdapter.notifyDataSetChanged();
                                }
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