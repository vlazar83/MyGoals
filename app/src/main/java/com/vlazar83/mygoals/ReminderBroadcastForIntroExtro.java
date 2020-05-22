package com.vlazar83.mygoals;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcastForIntroExtro extends BroadcastReceiver {

    protected static final String TITLE_FOR_INTRO_EXTRO = "TITLE_FOR_INTRO_EXTRO";
    protected static final String CONTENT_FOR_INTRO_EXTRO = "CONTENT_FOR_INTRO_EXTRO";


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notifyIntent = new Intent(MyGoals.getAppContext(), NotificationResultActivity.class);
        notifyIntent.putExtra(TITLE_FOR_INTRO_EXTRO, MyGoals.getAppContext().getString(R.string.NotificationIntroExtroTitle1));

        if(Settings.getInstance().getIsExtrovert() && Integer.valueOf(Utils.getLightGreenCardsCountFromWeek(StatisticsHolder.getInstance())) < 2){
            notifyIntent.putExtra(CONTENT_FOR_INTRO_EXTRO, MyGoals.getAppContext().getString(R.string.NotificationExtroText_more_private_time));
        } else if(!Settings.getInstance().getIsExtrovert() && Integer.valueOf(Utils.getGreenCardsCountFromWeek(StatisticsHolder.getInstance())) < 2){
            notifyIntent.putExtra(CONTENT_FOR_INTRO_EXTRO, MyGoals.getAppContext().getString(R.string.NotificationIntroText_more_private_time));
        }

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(MyGoals.getAppContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyMyGoals")
                .setSmallIcon(R.drawable.ic_add_alert_white_24dp)
                .setContentIntent(notifyPendingIntent)
                .setContentTitle(MyGoals.getAppContext().getString(R.string.NotificationIntroExtroTitle1))
                .setContentText(MyGoals.getAppContext().getString(R.string.NotificationIntroExtroText1))
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.dove_free_icon))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());

    }
}
