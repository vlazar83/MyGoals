package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.github.lzyzsd.circleprogress.CircleProgress;
import java.util.Timer;
import java.util.TimerTask;

public class StatisticsActivity extends AppCompatActivity{

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = findViewById(R.id.toolbar_for_statistics);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // to make the navigation back
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        CircleProgress circleProgressBlue = findViewById(R.id.circle_progress_blue);
        CircleProgress circleProgressGreen = findViewById(R.id.circle_progress_green);
        CircleProgress circleProgressRed = findViewById(R.id.circle_progress_red);

        circleProgressBlue.setFinishedColor(Color.BLUE);
        circleProgressGreen.setFinishedColor(Color.GREEN);
        circleProgressRed.setFinishedColor(Color.RED);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ObjectAnimator anim = ObjectAnimator.ofInt(circleProgressBlue, "progress", 0, 10);
                        ObjectAnimator anim2 = ObjectAnimator.ofInt(circleProgressGreen, "progress", 0, 50);
                        ObjectAnimator anim3 = ObjectAnimator.ofInt(circleProgressRed, "progress", 0, 100);
                        anim.setInterpolator(new DecelerateInterpolator());
                        anim.setDuration(500);
                        anim.start();

                        anim2.setInterpolator(new DecelerateInterpolator());
                        anim2.setDuration(500);
                        anim2.start();

                        anim3.setInterpolator(new DecelerateInterpolator());
                        anim3.setDuration(500);
                        anim3.start();

                    }
                });
            }
        }, 1000, 5000);

    }
}
