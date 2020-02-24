package com.wsiz.wd_mobile;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    ProgressBar splashProgress;
    int SPLASH_TIME = 2000;
    TextView logo2;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        splashProgress = findViewById(R.id.splashProgress);
        splashProgress.setScaleY(3f);
        logo2 = findViewById(R.id.logo2);
        logo = findViewById(R.id.logo);
        playProgress();
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

    private void handDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    Intent mySuperIntent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(mySuperIntent);
                    finish();

            }
        }, SPLASH_TIME);
    }
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(2000)
                .start();
    }



}