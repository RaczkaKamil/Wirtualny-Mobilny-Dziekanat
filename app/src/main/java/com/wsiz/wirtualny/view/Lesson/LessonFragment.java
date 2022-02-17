package com.wsiz.wirtualny.view.Lesson;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.JsonAdapter.JsonNews;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.Pocket.FileReader;
import com.wsiz.wirtualny.model.WSIZ_APP;
import com.wsiz.wirtualny.model.db.DatabaseDao;
import com.wsiz.wirtualny.model.db.RealmClasses.News;
import com.wsiz.wirtualny.model.db.RealmManager.RealmManager;
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

import io.realm.RealmResults;

public class LessonFragment extends Fragment {
    private static final String TAG = "LessonFragment";
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private TabLayout tabLayout;
    private boolean aLesson = true;

    PDFView pdfViewer;
    TextView pdf_name;

    String FILE_URL;
    String FILE_NAME;
    News final_news;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lesson, container, false);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.setToolbarVisible(false);

        pdfViewer = root.findViewById(R.id.pdfView);
        pdf_name = root.findViewById(R.id.pdf_name);



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


        DatabaseDao databaseDao = RealmManager.createDatabaseDao();
        RealmResults<News> news = databaseDao.getNewsFromBaseWithFile();
        final_news = news.get(0);
        System.out.println(final_news.toString());
        startDownload();

        return root;
    }

    private void startDownload() {

        this.FILE_URL = "https://dziekanat.wsi.edu.pl/news/file/" + final_news.getFileuuid()+"/"+final_news.getFilename();
        this.FILE_NAME = final_news.getFilename();
        System.out.println("CHECK PERM");
        if (WSIZ_APP.getInstance().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            String[] permision = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permision, PERMISSION_STORAGE_CODE);
        } else {
            startDownloading();
        }
    }


    private void startDownloading() {
        System.out.println("START DOWNLOAD");
        new Thread(this::DownloadFiles2).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownloading();
            } else {
                Log.e("SYNC getUpdate", "security denied");
            }
        }
    }

    public void DownloadFiles2() {
        try {



            System.out.println("Started dwonload");
            File futureStudioIconFile = new File(WSIZ_APP.getInstance().getFilesDir(),   FILE_NAME);


            System.out.println("CREATED FILE");
            InputStream inputStream = null;
            OutputStream outputStream = null;

            URL u = new URL(FILE_URL);
            HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", EasyPreferences.getCookies());

            System.out.println("DOWNLAD FILE");
             inputStream = connection.getInputStream();
            byte[] fileReader = new byte[4096];


            long fileSizeDownloaded = 0;

            System.out.println("TICK");
            outputStream = new FileOutputStream(futureStudioIconFile);
            System.out.println("TACK");
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }


                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;

            }
            System.out.println("TICKED");
            outputStream.flush();
             File file = new File(WSIZ_APP.getInstance().getFilesDir(),   FILE_NAME);



            System.out.println("IS FILE?" + file.exists());
            pdfViewer.post(new Runnable() {
                @Override
                public void run() {
                    pdf_name.setText(final_news.getTytul());
                    pdfViewer.fromFile(file).defaultPage(0).load();
                }
            });



        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SecurityException se) {
            se.printStackTrace();
        }

    }


    private void selectedTab(long id){
        if(id==0){
            aLesson=true;
        }else
        {
            aLesson=false;
        }


    }





}