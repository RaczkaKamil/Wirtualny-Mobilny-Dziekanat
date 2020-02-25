package com.wsiz.wd_mobile;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.google.gson.Gson;
import com.wsiz.wd_mobile.JsonAdapter.JsonUserID;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    String autoLogin = "";
    TextView tf_login;
    TextView tf_password;
    TextView tf_info;
    Button bt_login;
    Button btn_offline;
    JsonUserID jsonUserID;

    Snackbar bar;

    private boolean isAccoutSave = false;
    private boolean isTokenSave = false;
    private boolean isUserSave = false;

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
        connectLogin(login, encryptedPassword, password);
    }

    public void connectLogin(String login, String md5password, String password) {
        Thread thread = new Thread(() -> {
            try {
                changeSnackBarTExt("Logowanie...");
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/auth?album=" + login + "&pass=" + md5password);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                InputStream stream = connection.getInputStream();
                BufferedReader reader;
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                    Log.d("Response: ", "> " + line);
                    if (line.length() == 36) {
                        changeSnackBarTExt("Zapisywanie...");
                        saveToken(line);
                        connectGetID(line);
                        saveAccount(login, password);
                        LastStepOfLogin(true, true);
                    } else {
                        LastStepOfLogin(false, true);
                    }
                }

                connection.disconnect();
            } catch (Exception e) {
                LastStepOfLogin(false, false);
                e.printStackTrace();
            }
        });
        thread.start();
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
        int delay = 500;
        new Handler().postDelayed(() -> {
            if (isAccoutSave && isTokenSave && isUserSave) {
                Intent mySuperIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mySuperIntent);
                finish();
            } else {
                DelayLogin();
            }


        }, delay);
    }

    private void loginOffLine() {
        Intent mySuperIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mySuperIntent);
        finish();
    }

    private void saveAccount(String login, String password) {
        this.runOnUiThread(() -> {
            try {
                FileOutputStream fileOutputStream;
                fileOutputStream = getApplicationContext().openFileOutput("AccountLogin", Context.MODE_PRIVATE);
                fileOutputStream.write(login.getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(password.getBytes());
                fileOutputStream.close();
                System.out.println("-------------------ZAPISANO KONTO-----------------");
                System.out.println("KONTO: " + login + " " + password);
                isAccoutSave = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void saveToken(String token) {
        this.runOnUiThread(() -> {
            try {
                FileOutputStream fileOutputStream;
                fileOutputStream = getApplicationContext().openFileOutput("Token", Context.MODE_PRIVATE);
                fileOutputStream.write(token.getBytes());
                fileOutputStream.close();
                System.out.println("-------------------ZAPISANO TOKEN-----------------");
                System.out.println("TOKEN: " + token);
                isTokenSave = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void saveUser(JsonUserID jsonUserID) {
        this.runOnUiThread(() -> {
            try {
                FileOutputStream fileOutputStream;
                fileOutputStream = getApplicationContext().openFileOutput("AccountInfo", Context.MODE_PRIVATE);
                fileOutputStream.write(String.valueOf(jsonUserID.getStudentid()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.getAlbum()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.getImie()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.getNazwisko()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.getDataRejestracji()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.isActive()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.isStar()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.getFinid()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.getEmail()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.getPhone()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.write(String.valueOf(jsonUserID.getComment()).getBytes());
                fileOutputStream.write("/".getBytes());
                fileOutputStream.close();
                System.out.println("-------------------ZAPISANO INFORMACJE USERA-----------------");
                System.out.println("USER INFO: " + jsonUserID.getAlbum() + ", " + jsonUserID.getImie());
                changeSnackBarTExt("Zapisano...");
                isUserSave = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void connectGetID(String token) {
        changeSnackBarTExt("Pobieranie...");
        try {
            URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-auth/user?wdauth=" + token);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            InputStream stream = conn.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
                Log.d("Response: ", "> " + line);
                Gson gson = new Gson();
                jsonUserID = gson.fromJson(line, JsonUserID.class);
                saveUser(jsonUserID);
            }

            conn.disconnect();
        } catch (Exception e) {
            LastStepOfLogin(false, true);
            e.printStackTrace();
        }
    }
}
