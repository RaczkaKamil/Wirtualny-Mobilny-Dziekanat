package com.wsiz.wirtualny.view.Activity.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.Pocket.ConnectionMenager;
import com.wsiz.wirtualny.model.Pocket.EasyPreferences;
import com.wsiz.wirtualny.model.Pocket.FileReader;
import com.wsiz.wirtualny.model.Services.NotificationService;
import com.wsiz.wirtualny.model.network.manager.NetworkManager;
import com.wsiz.wirtualny.model.network.usecase.FetchFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetFinancesFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetGradesFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetLecturesFromServerUseCase;
import com.wsiz.wirtualny.model.network.usecase.GetNewsFromServerUseCase;
import com.wsiz.wirtualny.view.Finances.FinancesFragment;
import com.wsiz.wirtualny.view.Grade.GradeFragment;
import com.wsiz.wirtualny.view.Lesson.LessonFragment;
import com.wsiz.wirtualny.view.More.MoreFragment;
import com.wsiz.wirtualny.view.News.NewsFragment;

public class MainActivity extends AppCompatActivity {
    ConnectionMenager connectionMenager;
    Toolbar toolbar;
    int startingPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorWhite, this.getTheme()));

        BottomNavigationView navView = findViewById(R.id.nav_view_bottom);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("sa");
        setSupportActionBar(toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_news, R.id.nav_grade, R.id.nav_finances, R.id.nav_lesson, R.id.nav_more, R.id.nav_blank)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        @SuppressLint("CutPasteId") BottomNavigationView bottomNav = findViewById(R.id.nav_view_bottom);

        bottomNav.setOnNavigationItemSelectedListener(
                menuItem -> {
                    Fragment fragment = null;
                    int newPosition = 0;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_news:
                            fragment = new NewsFragment();
                            newPosition = 1;
                            break;
                        case R.id.nav_grade:
                            fragment = new GradeFragment();
                            newPosition = 2;
                            break;
                        case R.id.nav_lesson:
                            fragment = new LessonFragment();
                            newPosition = 3;
                            break;

                        case R.id.nav_finances:
                            fragment = new FinancesFragment();
                            newPosition = 4;
                            break;

                        case R.id.nav_more:
                            fragment = new MoreFragment();
                            newPosition = 5;
                            break;
                    }
                    return loadFragment(fragment, newPosition);
                }
        );

        loadFragment(new NewsFragment(), 1);
        downloadComponents();

        startMyService();


    }


    public boolean loadFragment(Fragment fragment, int newPosition) {
        System.out.println("FRAGMENT: " + newPosition);
        downloadComponent(newPosition);
        if (fragment != null) {
            if (startingPosition > newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.nav_host_fragment, fragment);
                getSupportFragmentManager().beginTransaction().remove(new NewsFragment()).commit();
                transaction.commit();
            } else if (startingPosition < newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.nav_host_fragment, fragment);
                getSupportFragmentManager().beginTransaction().remove(new NewsFragment()).commit();
                transaction.commit();
            }
            this.startingPosition = newPosition;
            return true;
        }
        return false;
    }

    public void downloadComponent(int fragment_position) {
        connectionMenager = new ConnectionMenager();
        switch (fragment_position) {
            case 1:
                connectionMenager.downloadNews();
            case 2:
                connectionMenager.downloadGrades();
                break;
            case 4:
                connectionMenager.downloadFinances();
                break;


        }
    }

    public void downloadComponents() {
        connectionMenager = new ConnectionMenager();
        connectionMenager.downloadData();
    }


    public boolean isSaved() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void startMyService() {
        //  Intent serviceIntent = new Intent(this, NotificationService.class);
        //  startService(serviceIntent);
    }

    public void setToolbarVisible(Boolean visible) {
        if (visible) {
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
        }

    }

}
