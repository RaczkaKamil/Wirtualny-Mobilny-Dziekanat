package com.wsiz.wirtualny.view.Activity.Start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.view.Activity.Login.LoginActivity;

public class StartActivity extends AppCompatActivity {

    int SPLASH_TIME = 2000;
    TextView logo2;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        logo2 = findViewById(R.id.logo2);
        logo = findViewById(R.id.logo);
        handDelay();
        logo.animate().alpha(1f).setDuration(2000);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().getDecorView()
                .setSystemUiVisibility(mUIFlag);

    }

    private void handDelay() {
        new Handler().postDelayed(() -> {

            Intent mySuperIntent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(mySuperIntent);
            finish();

        }, SPLASH_TIME);
    }




}