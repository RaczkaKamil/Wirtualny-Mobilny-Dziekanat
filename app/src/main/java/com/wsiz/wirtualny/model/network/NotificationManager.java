package com.wsiz.wirtualny.model.network;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.wsiz.wirtualny.R;
import com.wsiz.wirtualny.model.WSIZ_APP;
import com.wsiz.wirtualny.view.Activity.Main.MainActivity;

public class NotificationManager {

    public NotificationManager(String bigTextString, String setBigContentTitle, String setContentText){


        android.app.NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(WSIZ_APP.getInstance(), "notify_001");
        Intent ii = new Intent(WSIZ_APP.getInstance(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(WSIZ_APP.getInstance(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(bigTextString);
        bigText.setBigContentTitle(setBigContentTitle);
        bigText.setSummaryText(setContentText);


        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.logowsiz);
        mBuilder.setColor(Color.BLUE);
        mBuilder.setContentTitle(setBigContentTitle);
        mBuilder.setContentText(bigTextString);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (android.app.NotificationManager) WSIZ_APP.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    android.app.NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.BLUE);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

    }
}
