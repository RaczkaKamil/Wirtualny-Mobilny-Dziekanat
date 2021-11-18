package com.wsiz.wirtualny.view.Lesson;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.JsonAdapter.JsonNews;
import com.wsiz.wirtualny.model.Pocket.FileReader;
import com.wsiz.wirtualny.view.Activity.Login.LoginActivity;
import com.wsiz.wirtualny.view.Activity.Main.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class LessonFragment extends Fragment {
    private static final String TAG = "LessonFragment";
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private TabLayout tabLayout;
    private boolean aLesson = true;

    PDFView pdfViewer;
    TextView pdf_name;
    ArrayList<String> fileListPDF;
    ArrayList<String> fileListLessons;
    ArrayList<String> fileListOrganization;
    String FILE_URL;
    String FILE_NAME;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lesson, container, false);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.setToolbarVisible(false);

        pdfViewer = root.findViewById(R.id.pdfView);
        pdf_name = root.findViewById(R.id.pdf_name);
        fileListPDF = new ArrayList<>();
        fileListLessons = new ArrayList<>();
        fileListOrganization = new ArrayList<>();

        getNews();

        tabLayout = root.findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab((long) tab.getPosition());
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


    private void selectedTab(long id){
        if(id==0){
            aLesson=true;
        }else
        {
            aLesson=false;
        }

        getNews();
    }



    /////////////////////////////////////////////////////////ROKZLAD ZAJEC///////////////////////////////////////////




    private void getNews() {
        Thread thread = new Thread(() -> {
            try {
                MainActivity activity = (MainActivity) getActivity();
                boolean isNewsLoaded = false;
                assert activity != null;
                while (!activity.isSaved()) {
                    if (!isNewsLoaded) {
                        getAvailableNews("readed oldest news");
                        isNewsLoaded = true;
                    }
                }
                getAvailableNews("readed newest news");


            } catch (IOException | NullPointerException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }


    private void getAvailableNews(String message) throws IOException {
        try {
            String data;
            FileInputStream fileInputStream;
            fileInputStream = Objects.requireNonNull(getContext()).openFileInput(getContext().fileList()[getNewsFileNumber()]);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();


            while ((data = bufferedReader.readLine()) != null) {
                stringBuffer.append(data).append("\n");
                String splited = stringBuffer.toString();
                Log.d(TAG, message);
                Gson gson = new Gson();
                JsonNews[] jsonNews = gson.fromJson(splited, JsonNews[].class);
                setJson(jsonNews);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                Toast.makeText(getContext(), "Blad logowania!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            });
            e.fillInStackTrace();
        }

    }


    private int getNewsFileNumber() {
        for (int i = 0; i < Objects.requireNonNull(getContext()).fileList().length; i++) {
            if (getContext().fileList()[i].contains("News")) {
                return i;
            }
        }
        return -1;
    }

    private void setJson(JsonNews[] jsonNews) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            fileListLessons.clear();
            fileListOrganization.clear();
            for (JsonNews jsonNew : jsonNews) {
                if (jsonNew.getTytyl().contains("Rozkład zajęć na"))
                {
                    fileListLessons.add(
                            jsonNew.getTytyl() + "~~"
                                    + jsonNew.getFilename() + "~~"
                                    + jsonNew.getFileuuid() + "~~"
                                    + jsonNew.getDataut());
                }
                if (jsonNew.getTytyl().contains("Zarządzenie rektora")){
                    fileListOrganization.add(
                            jsonNew.getTytyl() + "~~"
                                    + jsonNew.getFilename() + "~~"
                                    + jsonNew.getFileuuid() + "~~"
                                    + jsonNew.getDataut());
                }

            }


            try{

                if(aLesson){
                    setNewestFileName(fileListLessons.get(0));
                }else{
                    setNewestFileName(fileListOrganization.get(0));
                }
            }catch (IndexOutOfBoundsException e){
                e.fillInStackTrace();
            }


        });
    }

    private void setNewestFileName(String name) {
        String[] NewestFile = name.split("~~");

        if (isNewestAlredySetted(NewestFile[1])) {

            setPdfView(NewestFile);
        } else {
            startDownload(NewestFile[1], NewestFile[2]);
        }
    }

    private void setPdfView(String[] NewestFile){
            File fileLesson = new File(Environment.getExternalStorageDirectory() + "/" + NewestFile[1]);
            pdf_name.setText(NewestFile[0]);
            if(aLesson){
                pdfViewer.fromFile(fileLesson).defaultPage(0).load();
            }else {
                pdfViewer.fromFile(fileLesson).defaultPage(1).load();
            }

    }

    private boolean isNewestAlredySetted(String newestName) {
        try{
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File f = new File(path);
        File[] file = f.listFiles();

        assert file != null;
        for (File value : file) {
            if (value.getName().contains(newestName)) {
                fileListPDF.add(value.getName());
                return true;
            }
        }
        }catch (NullPointerException e){
            e.fillInStackTrace();
        }

        return false;
    }

    private void startDownload(String fileName, String fileUUID) {
        FileReader fileReader = new FileReader();
        fileReader.startReadToken(getContext());
        this.FILE_URL = "https://dziekanat.wsi.edu.pl/news/file/" + fileUUID+"/"+fileName+"/" + "?wdauth=" + fileReader.getToken();
        this.FILE_NAME = fileName;
        if (Objects.requireNonNull(getContext()).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            String[] permision = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permision, PERMISSION_STORAGE_CODE);
        } else {
            startDownloading();
        }
    }

    private void startDownloading() {

        new Thread(this::DownloadFiles).start();
    }

    public void DownloadFiles() {
        try {

                File futureStudioIconFile = new File(Environment.getExternalStorageDirectory() + "/" + FILE_NAME);

                System.out.println("plik znajduje sie w " + futureStudioIconFile.getAbsolutePath());
                InputStream inputStream = null;
                OutputStream outputStream = null;

                URL u = new URL(FILE_URL);
                HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Cookie", "laravel_session=eyJpdiI6IitWZFNtY21TKzFFM1BhXC85ZEtaUGpnPT0iLCJ2YWx1ZSI6Ikp0d3FXODZ0RzhaZ3lHSVJlRFR5dFRFVTJqQWpZWW50K0NGU1hFd0t0aXN1dFwvQW44RlRGNWQwV3pRWnp3TnpPM0JlRldwalNwMkY5R25BdFpzSUJKZz09IiwibWFjIjoiOTc3OGUyZmI0NmVmZDVkMDk4NTkwN2QyNzY2MzQ4YmMzNTM5NzNjY2Y3NGE0ODg0ZTlmMTEwOWQxOTIzZWVjZiJ9");
                inputStream = connection.getInputStream();

                byte[] fileReader = new byte[4096];


                long fileSizeDownloaded = 0;

                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    System.out.println("tikam: " + read);
                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();


                File file = new File(Environment.getExternalStorageDirectory() + "/" + FILE_NAME);





            getNews();


        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownloading();
            } else {
                Log.e("SYNC getUpdate", "security denied");
            }
        }
    }




    /////////////////////////////////////////////////////////ORGANIZACJA ROKU///////////////////////////////////////////



}