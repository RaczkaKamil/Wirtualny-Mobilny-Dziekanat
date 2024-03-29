package com.wsiz.wirtualny.view.Activity.Login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.view.Activity.Main.MainActivity;
import com.wsiz.wirtualny.model.Pocket.ConnectionMenager;
import com.wsiz.wirtualny.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity {

    private TextView tf_login;
    private TextView tf_password;
    private TextView tf_info;
    private Button bt_login;
    private Button btn_offline;
    private Snackbar bar;
    private ConnectionMenager connectionMenager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colordarkgrey, this.getTheme()));

        bar = Snackbar.make(findViewById(android.R.id.content), "Łączenie...", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snack_view = (Snackbar.SnackbarLayout) bar.getView();
        snack_view.addView(new ProgressBar(this));
        connectionMenager = new ConnectionMenager();
        tf_login = findViewById(R.id.tf_login);
        tf_password = findViewById(R.id.tf_password);
        tf_info = findViewById(R.id.tf_info);
        bt_login = findViewById(R.id.bt_login);
        btn_offline = findViewById(R.id.btn_offline);
        tf_info.setAlpha(0f);
        bt_login.setOnClickListener(view ->
        {
            hideKeyboard(this);
            if(tf_login.getText().toString().length()>0){
                login();
            }

        });

        if (isAutoLoginEnable()) {
            SearchAndSetAccount();
            bt_login.callOnClick();
        } else {
            SearchAndSetAccount();
        }


        btn_offline.setOnClickListener(view -> loginOffLine());
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isAutoLoginEnable() {
        try {
            Intent intent = getIntent();
            autoLogin = intent.getStringExtra("AutoLogin");
             if (autoLogin.contains("false")) {
                System.out.println("ZABLOKOWANO LOGOWANIE!");
                return false;
            }else {
                return true;
            }
        } catch (NullPointerException e) {
            e.fillInStackTrace();
        }
        return true;
    }

    private void SearchAndSetAccount() {

                        tf_login.setText(EasyPreferences.getLogin());
                        tf_password.setText(EasyPreferences.getPassword());
    }
    private String autoLogin = "";

    private void login() {
        String login = tf_login.getText().toString().replaceAll("\\s","");
        String password = tf_password.getText().toString();
        String encryptedPassword = md5(password);
        bar.show();
        Log.d("LOGIN", "Login started");
        connectionMenager.Login(login, encryptedPassword, password, this);

    }

    public void showNetworkError(){
        tf_info.setText("Wystąpił problem z połączeniem!");
        tf_info.setTextColor(Color.RED);
        tf_info.animate().alpha(1f).setDuration(500);
        bar.dismiss();
    }


    public void showError(){
        tf_info.setText("Błędny Login lub Hasło!");
        tf_info.setTextColor(Color.RED);
        tf_info.animate().alpha(1f).setDuration(500);
        bar.dismiss();
    }

    public void delayLogin() {
        int delay = 200;
        new Handler().postDelayed(() -> {

                Log.d("LOGIN", "Login completed!");
                Intent mySuperIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mySuperIntent);
                finish();

        }, delay);
    }

    private void loginOffLine() {
        if(!EasyPreferences.getStudentID().equals("0")){
            Intent mySuperIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mySuperIntent);
            finish();
        }else{
            Toast.makeText(this, "Brak danych o poprzednich logowaniach", Toast.LENGTH_SHORT).show();
        }

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
