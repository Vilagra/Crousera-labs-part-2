package com.example.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Vilagra on 03.04.2017.
 */
public class AlarmReceiver extends BroadcastReceiver{
    private static final String TAG = "AlarmReceiver";

    public static final int NOTIFICATION_ID = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context.getApplicationContext(),
                0,
                new Intent(context.getApplicationContext(),MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context.getApplicationContext())
                .setContentTitle("Photo")
                .setContentText("Time for a photo")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setTicker("photo_time")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);

    }
}

