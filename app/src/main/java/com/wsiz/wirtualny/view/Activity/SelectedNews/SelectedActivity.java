package com.wsiz.wirtualny.view.Activity.SelectedNews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.Pocket.FileReader;
import com.wsiz.wirtualny.view.Activity.Main.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class SelectedActivity extends AppCompatActivity {
    private static final int PERMISSION_STORAGE_CODE = 1000;
    String TAG = "SELECTED";
    TextView tf_tytul;
    TextView tf_data;
    EditText tf_tresc;
     String[] chosed = new String[6];
    String FILE_URL;
    String FILE_NAME;
    PDFView pdfView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);
        Toolbar toolbar = findViewById(R.id.toolbar);
        pdfView = findViewById(R.id.pdfView);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Wiadomość");

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colordarkgrey, this.getTheme()));

        toolbar.showOverflowMenu();
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Wiadomość:");


        tf_tytul = findViewById(R.id.tf_tytul);
        tf_data = findViewById(R.id.tf_data);
        tf_tresc = findViewById(R.id.tf_tresc);



        try {
            this.chosed = getIntent().getStringArrayExtra("select");
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Błąd otwierania pliku", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Błąd otwierania pliku");
        }

        assert chosed != null;
        tf_tytul.setText(chosed[0]);
        Date date;
        date = new Date();
        date.setTime(Long.valueOf(chosed[1]));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        String dateFormated = format1.format(date);

        tf_data.setText(dateFormated);
        tf_tresc.setText(chosed[2]);
        tf_tresc.setKeyListener(null);





        pdfView.setVisibility(View.GONE);
        try {
            if (!chosed[4].contains("null")) {
                System.out.println("EXIST");
                 pdfView.setVisibility(View.VISIBLE);
               //  startDownload(chosed[4], chosed[5]);
                startDownloadToWebView(chosed[4], chosed[5]);
            }

        } catch (NullPointerException e) {
            e.fillInStackTrace();
        }
        FileReader fileReader = new FileReader();
        fileReader.startReadToken(this);

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectedActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_save) {
         startDownload(chosed[4], chosed[5]);
        //    startDownloadToWebView(chosed[4], chosed[5]);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void startDownloadToWebView(String fileName, String fileUUID) {
        FileReader fileReader = new FileReader();
        fileReader.startReadToken(this);
        this.FILE_URL = "https://dziekanat.wsi.edu.pl/news/file/" + fileUUID+"/"+fileName ;
        this.FILE_NAME = fileName;


        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            String[] permision = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permision, PERMISSION_STORAGE_CODE);
        } else {
            startDownloadingToWebView();
        }
    }


    private void startDownload(String fileName, String fileUUID) {
        FileReader fileReader = new FileReader();
        fileReader.startReadToken(this);
        this.FILE_URL = "https://dziekanat.wsi.edu.pl/news/file/" + fileUUID+"/"+fileName ;
        this.FILE_NAME = fileName;


        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

    private void startDownloadingToWebView() {

         new Thread(this::DownloadFiles2).start();
    }

    public void DownloadFiles2() {
        try {



                File futureStudioIconFile = new File(Environment.getExternalStorageDirectory() + "/" + FILE_NAME);

             InputStream inputStream = null;
            OutputStream outputStream = null;

            URL u = new URL(FILE_URL);
            HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Cookie", EasyPreferences.getCookies());
            inputStream = connection.getInputStream();


            byte[] fileReader = new byte[4096];


            long fileSizeDownloaded = 0;

            outputStream = new FileOutputStream(futureStudioIconFile);

            while (true) {
                int read = inputStream.read(fileReader);
                 if (read == -1) {
                    break;
                }


                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;

            }

            outputStream.flush();


            File file = new File(Environment.getExternalStorageDirectory() + "/" + FILE_NAME);
            pdfView.post(new Runnable() {
                @Override
                public void run() {
                    pdfView.fromFile(file).defaultPage(0).load();
                }
            });



        } catch (MalformedURLException mue) {
        } catch (IOException ioe) {
        } catch (SecurityException se) {
        }

    }
    public void DownloadFiles()   {
        try {
            File futureStudioIconFile = new File(Environment.getExternalStorageDirectory() + "/" + FILE_NAME);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            URL u = new URL(FILE_URL);
            HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", EasyPreferences.getCookies());
            inputStream = connection.getInputStream();

            byte[] fileReader = new byte[4096];


            long fileSizeDownloaded = 0;

            outputStream = new FileOutputStream(futureStudioIconFile);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);

                fileSizeDownloaded += read;

            }

            outputStream.flush();


            File file = new File(Environment.getExternalStorageDirectory() + "/" + FILE_NAME);
            pdfView.post(new Runnable() {
                @Override
                public void run() {
                    pdfView.fromFile(file).defaultPage(0).load();
                }
            });

            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);


        } catch (MalformedURLException mue) {
        } catch (IOException ioe) {
        } catch (SecurityException se) {
        }

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

}


