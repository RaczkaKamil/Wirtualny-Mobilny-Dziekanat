package com.wsiz.wd_mobile;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.wsiz.wd_mobile.Pocket.LocalConnectionWriter;
import com.wsiz.wd_mobile.Pocket.LocalFileReader;
import com.wsiz.wd_mobile.ui.Finances.FinancesFragment;
import com.wsiz.wd_mobile.ui.Grade.GradeFragment;
import com.wsiz.wd_mobile.ui.News.NewsFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String TOKEN;
    private String STUDENT_ID;
    private String FINANCES_ID;

    LocalConnectionWriter localConnectionWriter = new LocalConnectionWriter(this);
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colordarkgrey, this.getTheme()));

        LocalFileReader tokenPocket = new LocalFileReader();
        tokenPocket.startReadToken(this);
        tokenPocket.startReadUserID();
        this.TOKEN = tokenPocket.getToken();
        this.STUDENT_ID = String.valueOf(tokenPocket.getStudentid());
        this.FINANCES_ID = String.valueOf(tokenPocket.getFinid());


        localConnectionWriter.LocalNews(this.TOKEN);
        localConnectionWriter.LocalGrade(this.STUDENT_ID);
        localConnectionWriter.LocalFinances(this.FINANCES_ID);
        localConnectionWriter.LocalLectures(this.STUDENT_ID);

        System.out.println("______________ZALOGOWANO________________");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mySuperIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(mySuperIntent);
                finish();


            }
        });
        fab.setVisibility(View.GONE);
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
    }



    public boolean isSaved(){
        return localConnectionWriter.isAllComplete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_reload:
                Intent mySuperIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(mySuperIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
    }

}
