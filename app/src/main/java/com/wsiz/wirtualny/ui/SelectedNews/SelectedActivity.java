package com.wsiz.wirtualny.ui.SelectedNews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wsiz.wirtualny.MainActivity;
import com.wsiz.wirtualny.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectedActivity extends AppCompatActivity {

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
            System.out.println("bład otwierania");
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

        for (int i = 3; i < chosed.length; i++) {
            System.out.println(i + ". " + chosed[i]);
        }


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

    }



}

