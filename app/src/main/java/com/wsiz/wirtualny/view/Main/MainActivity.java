package com.wsiz.wirtualny.view.Main;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wsiz.wirtualny.model.Pocket.ConnectionMenager;
import com.wsiz.wirtualny.model.Pocket.FileReader;
import com.wsiz.wirtualny.R;

public class MainActivity extends AppCompatActivity {
    ConnectionMenager connectionMenager;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorWhite, this.getTheme()));


        BottomNavigationView navView = findViewById(R.id.nav_view_bottom);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_news, R.id.nav_grade, R.id.nav_finances, R.id.nav_lesson,R.id.nav_more)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       // DrawerLayout drawer = findViewById(R.id.drawer_layout);
       // NavigationView navigationView = findViewById(R.id.nav_view);
       // mAppBarConfiguration = new AppBarConfiguration.Builder(
       //         R.id.nav_news, R.id.nav_grade, R.id.nav_finances,
       //         R.id.nav_lesson, R.id.nav_personnel, R.id.nav_info)
      //          .setDrawerLayout(drawer)
      //          .build();
      //  NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
      //  NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
      //  NavigationUI.setupWithNavController(navigationView, navController);


        downloadComponents();

        startMyService();

    }

    public void downloadComponents(){
        FileReader fileReader = new FileReader();
        fileReader.startReadToken(this);
        fileReader.startReadUserID(this);

        connectionMenager=new ConnectionMenager(this);
        connectionMenager.LocalNews(fileReader.getToken());
        connectionMenager.LocalGrade(String.valueOf(fileReader.getStudentid()));
        connectionMenager.LocalFinances( String.valueOf(fileReader.getFinid()));
        connectionMenager.LocalLectures(String.valueOf(fileReader.getStudentid()));

    }



    public boolean isSaved() {
        return connectionMenager.isAllComplete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

  //  @Override
  //  public boolean onSupportNavigateUp() {
   //     NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
   //     return NavigationUI.navigateUp(navController, mAppBarConfiguration)
  //              || super.onSupportNavigateUp();
   // }
///
   // private AppBarConfiguration mAppBarConfiguration;





    private void startMyService() {
        //Intent serviceIntent = new Intent(this, NotificationService.class);
       // startService(serviceIntent);
    }

    public void setToolbarVisible(Boolean visible){
        if(visible){
            toolbar.setVisibility(View.VISIBLE);
        }else {
            toolbar.setVisibility(View.GONE);
        }

    }

}
