package com.wsiz.wd_mobile.ui.SelectedNews;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.wsiz.wd_mobile.MainActivity;
import com.wsiz.wd_mobile.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class SelectedActivity extends AppCompatActivity {

    TextView tf_tytul;
    TextView tf_data;
    EditText tf_tresc;
    Button btn_download;
    String chosed[]=new String[6];
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
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Wiadomość:");

        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        tf_tytul=findViewById(R.id.tf_tytul);
        tf_data=findViewById(R.id.tf_data);
        tf_tresc=findViewById(R.id.tf_tresc);
        btn_download=findViewById(R.id.btn_download);
        btn_download.setVisibility(View.GONE);


        try{
            this.chosed = getIntent().getStringArrayExtra("select");
        }catch (NumberFormatException e)
        {
            System.out.println("bład otwierania");
        }

        tf_tytul.setText(chosed[0]);
        Date date;
        date = new Date();
        date.setTime(Long.valueOf(chosed[1]));
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        String dateFormated = format1.format(date);

        tf_data.setText(dateFormated);
        tf_tresc.setText(chosed[2]);
        tf_tresc.setKeyListener(null);

        for (int i = 3; i < chosed.length; i++) {
            System.out.println(i+". "+ chosed[i]);
        }


        try{
            if(!chosed[4].contains("null")){
                btn_download.setText(chosed[4]);
                btn_download.setVisibility(View.VISIBLE);
                btn_download.setOnClickListener(view -> {
                    startDownload(chosed[4],chosed[5]);
                });
            }

        }catch (NullPointerException e){
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }


    private void startDownload(String fileName, String fileUUID){
}
private boolean check(){
    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        Log.e("Permission error","You have permission");
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


public void download2(String uriname){
if(check()) {


    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uriname));
    request.setDescription("Download PDF");
    request.setTitle("From WSIZ BB");
// in order for this if to run, you must use the android 3.2 to compile your app

    request.allowScanningByMediaScanner();
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.pdf");

// get download service and enqueue file
    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    manager.enqueue(request);
}else{
    System.out.println("Brak uprawnien!");
}
}
private long downloadID;
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(SelectedActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                System.out.println("OPENING PDF");

                File file = new File(getExternalFilesDir(null),"Dummy");
                if (file.exists()) {
                    //File file2 = new File(targetPdf);
                    Uri uri = FileProvider.getUriForFile(SelectedActivity.this, getPackageName() + ".provider", file);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else
                    Toast.makeText(SelectedActivity.this, "Błąd podczas otwierania", Toast.LENGTH_SHORT).show();

            }
        }
    };

    private void beginDownload(String Uriname){
        File file=new File(getExternalFilesDir(null),"Dummy");
        /*
        Create a DownloadManager.Request with all the information necessary to start the download
         */
        DownloadManager.Request request= null;// Set if download is allowed on roaming network
            request = new DownloadManager.Request(Uri.parse(Uriname))
                    .setTitle("Dummy File")// Title of the Download Notification
                    .setDescription("Downloading")// Description of the Download Notification
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                    .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                   // .setRequiresCharging(false)// Set if charging is required to begin the download
                    .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                    .setAllowedOverRoaming(true);

        DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
    }


    private void download1(String fileName, String fileUUID){
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            //  PDFTools.showPDFUrl(this, "https://dziekanat.wsi.edu.pl/news/file/"+fileUUID+"/"+fileName);
            String Uriname = "https://dziekanat.wsi.edu.pl/news/file/" + fileUUID + "/" + fileName;
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Uriname)));
            System.out.println("OTWIERAM");
            System.out.println(Uriname);
            URL url = null;
            try {
                url = new URL(Uriname);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();
                InputStream stream = conn.getInputStream();
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                System.out.println(conn.getResponseMessage());

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    //  Log.d("Response: ", "> " + line);
                    //  System.out.println(line);

                }

                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.fillInStackTrace();
            }

        }
    });
    thread.start();
}
    }

