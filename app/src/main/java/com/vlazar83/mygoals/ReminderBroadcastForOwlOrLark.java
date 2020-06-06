package com.vlazar83.mygoals;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcastForOwlOrLark extends BroadcastReceiver {

    protected static final String TITLE_FOR_OWL_OR_LARK = "TITLE_FOR_OWL_OR_LARK";
    protected static final String CONTENT_FOR_OWL_OR_LARK = "CONTENT_FOR_OWL_OR_LARK";


    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notifyIntent = new Intent(MyGoals.getAppContext(), NotificationResultActivity.class);
        notifyIntent.putExtra(TITLE_FOR_OWL_OR_LARK, MyGoals.getAppContext().getString(R.string.Notification_owl_or_lark_title));
        notifyIntent.putExtra(CONTENT_FOR_OWL_OR_LARK, MyGoals.getAppContext().getString(R.string.Notification_owl_or_lark_text));
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(MyGoals.getAppContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyMyGoals")
                .setSmallIcon(R.drawable.ic_add_alert_white_24dp)
                .setContentIntent(notifyPendingIntent)
                .setContentTitle(MyGoals.getAppContext().getString(R.string.Notification_owl_or_lark_title))
                .setContentText(MyGoals.getAppContext().getString(R.string.NotificationGoldenSentenceText1))
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.dove_free_icon))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());

    }
}
