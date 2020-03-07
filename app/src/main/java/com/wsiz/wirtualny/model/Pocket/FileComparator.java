package com.wsiz.wirtualny.model.Pocket;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.wsiz.wirtualny.view.Main.MainActivity;
import com.wsiz.wirtualny.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

class FileComparator {
    private Context ctx;
    private int newData;
    private String fileName;

    FileComparator(int newData, String fileName, Context context) {
        this.ctx = context;
        this.fileName = fileName;
        this.newData = newData;
            getAvailableData();
    }

    private int getFileNumber() {
        for (int i = 0; i < Objects.requireNonNull(ctx).fileList().length; i++) {
            if (ctx.fileList()[i].contains(fileName)) {
                return i;
            }
        }
        return -1;
    }

    private void getAvailableData() {
        try {
            String data;
            FileInputStream fileInputStream;
            fileInputStream = Objects.requireNonNull(ctx).openFileInput(ctx.fileList()[getFileNumber()]);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();


            while ((data = bufferedReader.readLine()) != null) {
                stringBuffer.append(data).append("\n");
                int lastData = data.length();
                if(lastData !=newData){
                    startNottification();
                }
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.fillInStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startNottification(){
        String bigTextString = null;
        String BigContentTitleString = null;
        String SummaryTextString = null;

        if (fileName.contains("News")) {
            SummaryTextString="Aktualności";
            BigContentTitleString="Nowe aktualności:";
            bigTextString="Wykryto nowe aktualności.";
        }else if(fileName.contains("Grade")){
            SummaryTextString="Oceny";
            BigContentTitleString="Nowe oceny:";
            bigTextString="Wykryto nowe oceny!";
        }else if (fileName.contains("Finances")){
            SummaryTextString="Finanse";
            BigContentTitleString="Nowe finanse:";
            bigTextString="Wykryto nowe zarejestrowane wpłaty.";
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx.getApplicationContext(), "notify_001");
        Intent ii = new Intent(ctx.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx.getApplicationContext(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(bigTextString);
        bigText.setBigContentTitle(BigContentTitleString);
        bigText.setSummaryText(SummaryTextString);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.logowsiz);
        mBuilder.setColor(Color.BLUE);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setStyle(bigText);
         NotificationManager mNotificationManager =
                (NotificationManager) ((Activity) ctx).getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.BLUE);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        assert mNotificationManager != null;
        mNotificationManager.notify(0, mBuilder.build());
    }
}
