package com.vlazar83.mygoals;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    protected static final String TITLE = "TITLE";
    protected static final String CONTENT = "CONTENT";


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notifyIntent = new Intent(MyGoals.getAppContext(), NotificationResultActivity.class);
        notifyIntent.putExtra(TITLE, "this is the title");
        notifyIntent.putExtra(CONTENT, "blablasdgfdsfdgdfgdfgdfg" + System.getProperty ("line.separator") + "sdfdsfsdf" +System.getProperty ("line.separator"));
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(MyGoals.getAppContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyMyGoals")
                .setSmallIcon(R.drawable.ic_add)
                .setContentIntent(notifyPendingIntent)
                .setContentTitle("Reminder")
                .setContentText("Hello there")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());

    }
}
