package com.wsiz.wirtualny.model.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.wsiz.wirtualny.view.Activity.Main.MainActivity;
import com.wsiz.wirtualny.model.Pocket.FileReader;
import com.wsiz.wirtualny.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class NotificationService  extends Service {
    private Toast toast;
    private Timer timer;
    private TimerTask timerTask;
    private String TOKEN;
    private NotificationManager mNotificationManager;

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
           // startConntect();
        }
    }

    private void setNotification(String bigTextString,String setBigContentTitle, String setContentText){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(bigTextString);
        bigText.setBigContentTitle(setBigContentTitle);
        bigText.setSummaryText(setContentText);


        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.logowsiz);
        mBuilder.setColor(Color.BLUE);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.BLUE);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

    }



    private void writeToLogs(String message) {
        Log.d("HelloServices", message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        writeToLogs("Called onCreate() method.");
        timer = new Timer();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        FileReader fileReader = new FileReader();
        fileReader.startReadToken(this);
        this.TOKEN = fileReader.getToken();

    }

    private void startConntect(){
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("https://dziekanat.wsi.edu.pl/get/wd-news/news?wdauth=" + TOKEN);
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
                    setNotification("Rozkład zajęć","Rozkład zajęć na tydzień 1","Nowe ogłoszenie");
                }

                conn.disconnect();

            } catch (Exception e) {
            e.fillInStackTrace();
            }
        });
        thread.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        writeToLogs("Called onStartCommand() methond");
        clearTimerSchedule();
        initTask();
        timer.scheduleAtFixedRate(timerTask, 10 * 1000, 10 * 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    private void clearTimerSchedule() {
        if(timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
    }

    private void initTask() {
        timerTask = new MyTimerTask();
    }

    @Override
    public void onDestroy() {
        writeToLogs("Called onDestroy() method");
        clearTimerSchedule();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
