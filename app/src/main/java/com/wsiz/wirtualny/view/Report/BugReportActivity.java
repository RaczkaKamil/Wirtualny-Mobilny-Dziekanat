package com.wsiz.wirtualny.view.Report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.ListAdapter.BugListAdapter;
import com.wsiz.wirtualny.view.Main.MainActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class BugReportActivity extends AppCompatActivity {
    EditText bug_track;
    Button coppy_text;
    private ArrayList<String> LogList = new ArrayList<>();
    private ArrayList<String> messageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        bug_track = findViewById(R.id.bug_track);
        coppy_text = findViewById(R.id.coppy_text);
        bug_track.setVisibility(View.GONE);
        coppy_text.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Zgłaszanie problemu");
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colordarkgrey, this.getTheme()));
        toolbar.showOverflowMenu();
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Zgłaszanie problemu");




        messageList.add("Zgłoś błąd z news'ami");
        messageList.add("Zgłoś błąd z ocenami");
        messageList.add("Zgłoś błąd z finansami");
        BugListAdapter customAdapterr = new BugListAdapter(messageList, this);
        final ListView online_list = findViewById(R.id.list_bug);
        online_list.setAdapter(customAdapterr);
        online_list.setClickable(false);
        customAdapterr.notifyDataSetChanged();

        loadLogFile();
        online_list.setOnItemClickListener((adapterView, view, i, l) -> {
            int metchodID = (int) l;
            setRaportMetchod(metchodID);
        });


        coppy_text.setOnClickListener(view -> {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", bug_track.getText());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);

        });

    }

    private void setRaportMetchod(int metchodID) {
        if( metchodID == 0){
            sendEmail(0);
        }else if (metchodID ==1){
            buildAlertMessage(" ocenach",1);
        }else if(metchodID ==2){
            buildAlertMessage(" finansach",2);
        }
    }

    @SuppressLint("SetTextI18n")
    private void sendEmail(int metchod){
        System.out.println(metchod);
        bug_track.setVisibility(View.VISIBLE);
        coppy_text.setVisibility(View.VISIBLE);
        if(metchod ==0){
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={"raczka.kamil@onet.pl"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"WD - MOBILE REPORT BUG ( NEWS )");
            intent.putExtra(Intent.EXTRA_TEXT,getFileInfo());
            bug_track.setText(getFileInfo());
            intent.putExtra(Intent.EXTRA_CC,"raczka.kamil@onet.pl");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        }else if (metchod==1) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={"raczka.kamil@onet.pl"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"WD - MOBILE REPORT BUG ( OCENY )");
            intent.putExtra(Intent.EXTRA_TEXT,getFileInfo()+"\n"+ getGradeInfo());
            bug_track.setText(getFileInfo()+"\n"+ getGradeInfo());
            intent.putExtra(Intent.EXTRA_CC,"raczka.kamil@onet.pl");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        } else if (metchod == 2 ) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={"raczka.kamil@onet.pl"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"WD - MOBILE REPORT BUG ( FINANSE )");
            intent.putExtra(Intent.EXTRA_TEXT,getFileInfo()+"\n"+getFinancesInfo());
            bug_track.setText(getFileInfo()+"\n"+ getFinancesInfo());
            intent.putExtra(Intent.EXTRA_CC,"raczka.kamil@onet.pl");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        }

    }

    private String getFileInfo() {

      StringBuilder fileListString = new StringBuilder("FILE LIST: " + "\n");

        fileListString.append(fileList()[0]).append("\n");
        for (int i = 1; i <fileList().length; i++) {
            fileListString.append(fileList()[i]).append("\n");
        }


        return fileListString.toString();
    }

    private String getGradeInfo() {
        try {
            return getAvailableGrade();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    private String getFinancesInfo() {
        try {
            return getAvailableFinances();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error";

    }

    private void loadLogFile() {
            LogList.add("\n"+"Started getGrade:");
            Thread thread = new Thread(() -> {
                try {
                    getAvailableGrade();
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        }

    private String getAvailableGrade() throws IOException {
        String data;
        FileInputStream fileInputStream;
        fileInputStream = Objects.requireNonNull(this).openFileInput(this.fileList()[getGradeFileNumber()]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuffer = new StringBuilder();


        while ((data = bufferedReader.readLine()) != null) {
            stringBuffer.append(data).append("\n");
            String splited = stringBuffer.toString();
            LogList.add("\n"+"getAvailableGrade:");
            LogList.add(splited);
            return splited;
        }
        return "error";
    }
    private int getGradeFileNumber() {
        for (int i = 0; i < Objects.requireNonNull(this).fileList().length; i++) {
            if (this.fileList()[i].contains("Grade")) {
                return i;
            }
        }
        return -1;
    }
    private void buildAlertMessage(String message, int option) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aby przesłać raport, musisz zezwolić na wysłanie do działu technicznego danych o twoich " + message)
                .setCancelable(true)
                .setPositiveButton("Zezwalam", (dialog, id) -> {
                    dialog.cancel();
                    sendEmail(option);
                })
                .setNegativeButton("Odmawiam", (dialog, id) -> {

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);});
        final AlertDialog alert = builder.create();
        alert.show();

    }


    private String getAvailableFinances() throws IOException {
        String data;
        FileInputStream fileInputStream;
        fileInputStream = Objects.requireNonNull(this).openFileInput(this.fileList()[getFinancesFileNumber()]);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuffer = new StringBuilder();


        while ((data = bufferedReader.readLine()) != null) {
            stringBuffer.append(data).append("\n");
            return stringBuffer.toString();

        }
        return "ERROR";
    }

    private int getFinancesFileNumber() {
        for (int i = 0; i < Objects.requireNonNull(this).fileList().length; i++) {
            if (this.fileList()[i].contains("Finances")) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
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

}
