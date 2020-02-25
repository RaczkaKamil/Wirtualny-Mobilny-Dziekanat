package com.wsiz.wd_mobile;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.wsiz.wd_mobile.Pocket.LocalConnectionWriter;
import com.wsiz.wd_mobile.Pocket.LocalFileReader;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity {

    LocalConnectionWriter localConnectionWriter = new LocalConnectionWriter(this);
    Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;

    String TOKEN;
    String STUDENT_ID;
    String FINANCES_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colordarkgrey, this.getTheme()));
        LocalFileReader tokenPocket = new LocalFileReader();
        tokenPocket.startReadToken(this);
        tokenPocket.startReadUserID();
        TOKEN = tokenPocket.getToken();
        STUDENT_ID = String.valueOf(tokenPocket.getStudentid());
        FINANCES_ID = String.valueOf(tokenPocket.getFinid());

        localConnectionWriter.LocalNews(TOKEN);
        localConnectionWriter.LocalGrade(STUDENT_ID);
        localConnectionWriter.LocalFinances(FINANCES_ID);
        localConnectionWriter.LocalLectures(STUDENT_ID);

        System.out.println("______________ZALOGOWANO________________");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_news, R.id.nav_grade, R.id.nav_finances,
                R.id.nav_lesson, R.id.nav_personnel, R.id.nav_info)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        startMyService();

    }

    private void refresh(){
        Intent mySuperIntent = new Intent(this, MainActivity.class);
        startActivity(mySuperIntent);
        Toast.makeText(this,"Odświeżono połączenie",Toast.LENGTH_SHORT).show();
    }


    public boolean isSaved() {
        return localConnectionWriter.isAllComplete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reload) {
           refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        toolbar.setTitle(title);
    }

    private void startMyService() {
        System.out.println("ZACZETO SERWIS");
        //Intent serviceIntent = new Intent(this, NotificationService.class);
       // startService(serviceIntent);


    }

}
