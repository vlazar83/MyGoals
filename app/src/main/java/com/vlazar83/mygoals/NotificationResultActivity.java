package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationResultActivity extends AppCompatActivity {

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
        }

    }
}
