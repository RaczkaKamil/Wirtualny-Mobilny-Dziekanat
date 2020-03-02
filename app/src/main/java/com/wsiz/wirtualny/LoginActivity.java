package com.wsiz.wirtualny;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.wsiz.wirtualny.Pocket.ConnectionMenager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Observable;
import io.reactivex.Observer;


public class LoginActivity extends AppCompatActivity{
    String autoLogin = "";
    TextView tf_login;
    TextView tf_password;
    TextView tf_info;
    Button bt_login;
    Button btn_offline;

    Snackbar bar;


    ConnectionMenager connectionMenager = new ConnectionMenager(this);

    private Observable<ConnectionMenager> emiter;
    private Observer<ConnectionMenager> observer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colordarkgrey, this.getTheme()));

        bar = Snackbar.make(findViewById(android.R.id.content), "Łączenie...", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) bar.getView();
        snack_view.addView(new ProgressBar(this));


        tf_login = findViewById(R.id.tf_login);
        tf_password = findViewById(R.id.tf_password);
        tf_info = findViewById(R.id.tf_info);
        bt_login = findViewById(R.id.bt_login);
        btn_offline = findViewById(R.id.btn_offline);
        tf_info.setAlpha(0f);

        if (isAutoLoginEnable()) {
            SearchAndSetAccount();
            login();
        } else {
            SearchAndSetAccount();
        }

        bt_login.setOnClickListener(view -> login());
        btn_offline.setOnClickListener(view -> loginOffLine());
    }

    private boolean isAutoLoginEnable() {
        try {
            Intent intent = getIntent();
            autoLogin = intent.getStringExtra("AutoLogin");
            assert autoLogin != null;
            if (autoLogin.contains("false")) {
                System.out.println("ZABLOKOWANO LOGOWANIE!");
                return false;
            }
        } catch (NullPointerException e) {
            e.fillInStackTrace();
        }
        return true;
    }

    private void SearchAndSetAccount() {
        String data;
        for (int i = 0; i < fileList().length; i++) {
            if (this.fileList()[i].contains("AccountLogin")) {
                try {
                    FileInputStream fileInputStream;
                    fileInputStream = this.openFileInput(this.fileList()[i]);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuffer = new StringBuilder();
                    while ((data = bufferedReader.readLine()) != null) {
                        stringBuffer.append(data).append("\n");
                        String splited = stringBuffer.toString();
                        String[] account = splited.split("/");
                        tf_login.setText(account[0]);
                        tf_password.setText(account[1].trim());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void login() {



        String login = tf_login.getText().toString();
        String password = tf_password.getText().toString();
        String encryptedPassword = md5(password);
        bar.show();
        Log.d("LOGIN","Login started");
        connectionMenager.Login(login, encryptedPassword, password);
       DelayLogin();


    }

    @SuppressLint("SetTextI18n")
    private void LastStepOfLogin(boolean isSucces, boolean isConnect) {
        this.runOnUiThread(() -> {
            tf_info.setAlpha(0f);
            if (isSucces && isConnect) {
                tf_info.setText("Zalogowano");
                changeSnackBarTExt("Ładowanie...");
                tf_info.setTextColor(Color.GREEN);
                tf_info.animate().alpha(1f).setDuration(500);
                DelayLogin();
            } else if (!isSucces && isConnect) {
                tf_info.setText("Błędny Login lub Hasło!");
                tf_info.setTextColor(Color.RED);
                tf_info.animate().alpha(1f).setDuration(500);
            } else if (!isSucces) {
                tf_info.setText("Błąd serwera!");
                tf_info.setTextColor(Color.RED);
                tf_info.animate().alpha(1f).setDuration(500);
            }
        });
    }

    private void changeSnackBarTExt(String text) {
        this.runOnUiThread(() -> bar.setText(text).show());

    }

    private void DelayLogin() {
        int delay = 200;
        new Handler().postDelayed(() -> {
    if(connectionMenager.fileSaver.isLoginComplete()){
        Log.d("LOGIN","Login completed!");
    Intent mySuperIntent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(mySuperIntent);
    finish();
}else {
    DelayLogin();
}

        }, delay);
    }

    private void loginOffLine() {
        Intent mySuperIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mySuperIntent);
        finish();
    }


    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();


            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2)
                    h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
