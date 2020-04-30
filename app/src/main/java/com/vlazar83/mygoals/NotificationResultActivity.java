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
        title.setText(intent.getStringExtra(ReminderBroadcast.TITLE));
        content.setText(intent.getStringExtra(ReminderBroadcast.CONTENT));
    }
}
