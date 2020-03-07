package com.wsiz.wirtualny.view.SelectedNews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.wsiz.wirtualny.view.Main.MainActivity;
import com.wsiz.wirtualny.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectedActivity extends AppCompatActivity {
    String TAG ="SELECTED";
    TextView tf_tytul;
    TextView tf_data;
    EditText tf_tresc;
    Button btn_download;
    String[] chosed = new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        btn_download = findViewById(R.id.btn_download);
        btn_download.setVisibility(View.GONE);


        try {
            this.chosed = getIntent().getStringArrayExtra("select");
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Błąd otwierania pliku", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Błąd otwierania pliku");
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


        try {
            if (!chosed[4].contains("null")) {
                btn_download.setText(chosed[4]);
                btn_download.setVisibility(View.VISIBLE);
                btn_download.setOnClickListener(view -> startDownload(chosed[4], chosed[5]));
            }

        } catch (NullPointerException e) {
            e.fillInStackTrace();
        }


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
        return false;
    }

    private void startDownload(String fileName, String fileUUID) {
        String Uriname = "https://dziekanat.wsi.edu.pl/news/file/" + fileUUID + "/" + fileName;
        Toast.makeText(this, "Funkcja pobierania będzie niedługo dostępna...", Toast.LENGTH_SHORT).show();
       // download(Uriname,fileName);
    }

    public void download(String uriname,String fileName){
        if(checkPermission()) {

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uriname));
            request.setDescription(fileName);
            request.setTitle(fileName);

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            assert manager != null;
            manager.enqueue(request);
        }else{
            Toast.makeText(this, "Brak uprawnien!", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Brak uprawnień!");
        }
    }


    private boolean checkPermission(){
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){



        }
    }

}

