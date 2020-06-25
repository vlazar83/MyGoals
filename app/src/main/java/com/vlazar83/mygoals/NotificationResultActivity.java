package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationResultActivity extends AppCompatActivity {

    protected static final String AGE_TITLE = "AGE_TITLE";
    protected static final String AGE_CONTENT = "AGE_CONTENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_result);

        TextView title = findViewById(R.id.notificationTitle);
        TextView content = findViewById(R.id.notificationContent);

        Intent intent = getIntent();
        if(intent.getStringExtra(ReminderBroadcast.TITLE) != null && !intent.getStringExtra(ReminderBroadcast.TITLE).equalsIgnoreCase("") ){
            title.setText(intent.getStringExtra(ReminderBroadcast.TITLE));
            content.setText(intent.getStringExtra(ReminderBroadcast.CONTENT));
        } else if(intent.getStringExtra(ReminderBroadcastForIntroExtro.TITLE_FOR_INTRO_EXTRO) != null && !intent.getStringExtra(ReminderBroadcastForIntroExtro.TITLE_FOR_INTRO_EXTRO).equalsIgnoreCase("") ){
            title.setText(intent.getStringExtra(ReminderBroadcastForIntroExtro.TITLE_FOR_INTRO_EXTRO));
            content.setText(intent.getStringExtra(ReminderBroadcastForIntroExtro.CONTENT_FOR_INTRO_EXTRO));
        } else if(intent.getStringExtra(ReminderBroadcastForOwlOrLark.TITLE_FOR_OWL_OR_LARK) != null && !intent.getStringExtra(ReminderBroadcastForOwlOrLark.TITLE_FOR_OWL_OR_LARK).equalsIgnoreCase("") ){
            title.setText(intent.getStringExtra(ReminderBroadcastForOwlOrLark.TITLE_FOR_OWL_OR_LARK));
            content.setText(intent.getStringExtra(ReminderBroadcastForOwlOrLark.CONTENT_FOR_OWL_OR_LARK));
        } else if(intent.getStringExtra(NotificationResultActivity.AGE_TITLE) != null && !intent.getStringExtra(NotificationResultActivity.AGE_TITLE).equalsIgnoreCase("") ){
            title.setText(intent.getStringExtra(NotificationResultActivity.AGE_TITLE));
            content.setText(intent.getStringExtra(NotificationResultActivity.AGE_CONTENT));
        }

    }
}
