package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class StatisticsActivity extends AppCompatActivity{

    private Timer timer;
    private StatisticsHolder statisticsHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = findViewById(R.id.toolbar_for_statistics);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // to make the navigation back
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        loadSettingsFromSharedPreferences();

        CircleProgress circleProgressBlue = findViewById(R.id.circle_progress_blue);
        CircleProgress circleProgressGreen = findViewById(R.id.circle_progress_green);
        CircleProgress circleProgressRed = findViewById(R.id.circle_progress_red);

        TextView blueCardsCountTextView = findViewById(R.id.textView_blue_cards_count);
        TextView redCardsCountTextView = findViewById(R.id.textView_red_cards_count);
        TextView greenCardsCountTextView = findViewById(R.id.textView_green_cards_count);

        statisticsHolder = StatisticsHolder.getInstance();
        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        blueCardsCountTextView.setText(String.valueOf(statisticsHolder.getStatistic(today).getBlueCardCount()) + "/" + Utils.getBlueCardsCountFromWeek(statisticsHolder));
        redCardsCountTextView.setText(String.valueOf(statisticsHolder.getStatistic(today).getRedCardCount()) + "/" + Utils.getRedCardsCountFromWeek(statisticsHolder));
        greenCardsCountTextView.setText(String.valueOf(statisticsHolder.getStatistic(today).getGreenCardCount() + statisticsHolder.getStatistic(today).getLightGreenCardCount()) + "/" + String.valueOf(Integer.valueOf(Utils.getGreenCardsCountFromWeek(statisticsHolder)) + Integer.valueOf(Utils.getLightGreenCardsCountFromWeek(statisticsHolder))));

        circleProgressBlue.setFinishedColor(Color.BLUE);
        circleProgressGreen.setFinishedColor(Color.GREEN);
        circleProgressRed.setFinishedColor(Color.RED);

        timer = new Timer();
        int redMax = 0, greenMax = 0, blueMax = 0, redMax2, greenMax2, blueMax2;
        if((Integer.valueOf(Utils.getRedCardsCountFromWeek(statisticsHolder))*100/Settings.getInstance().getWeekly_target_red_card()) > 100) redMax = 100; else{
            redMax =Integer.valueOf(Utils.getRedCardsCountFromWeek(statisticsHolder))*100/Settings.getInstance().getWeekly_target_red_card();
        }

        if((Integer.valueOf(Utils.getGreenCardsCountFromWeek(statisticsHolder))*100/Settings.getInstance().getWeekly_target_green_card()) > 100) greenMax = 100; else{
            greenMax =Integer.valueOf(Utils.getGreenCardsCountFromWeek(statisticsHolder))*100/Settings.getInstance().getWeekly_target_green_card();
        }

        if((Integer.valueOf(Utils.getBlueCardsCountFromWeek(statisticsHolder))*100/Settings.getInstance().getWeekly_target_blue_card()) > 100) blueMax = 100; else{
            blueMax =Integer.valueOf(Utils.getBlueCardsCountFromWeek(statisticsHolder))*100/Settings.getInstance().getWeekly_target_blue_card();
        }

        redMax2 = redMax;
        greenMax2 = greenMax;
        blueMax2 = blueMax;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ObjectAnimator anim = ObjectAnimator.ofInt(circleProgressBlue, "progress", 0, blueMax2);
                        ObjectAnimator anim2 = ObjectAnimator.ofInt(circleProgressGreen, "progress", 0, greenMax2);
                        ObjectAnimator anim3 = ObjectAnimator.ofInt(circleProgressRed, "progress", 0, redMax2);
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

        if( Settings.getInstance().getIsExtrovert()){
            if(Integer.valueOf(Utils.getGreenCardsCountFromWeek(statisticsHolder)) < 1){
                Toast.makeText(StatisticsActivity.this, MyGoals.getAppContext().getString(R.string.StatisticsActivity_Toast_extrovert), Toast.LENGTH_LONG).show();
            }
        } else {
            if(Integer.valueOf(Utils.getLightGreenCardsCountFromWeek(statisticsHolder)) < 1){
                Toast.makeText(StatisticsActivity.this, MyGoals.getAppContext().getString(R.string.StatisticsActivity_Toast_introvert), Toast.LENGTH_LONG).show();
            }
        }


    }

    private void loadSettingsFromSharedPreferences(){

        Gson gson = new Gson();
        String settingsJsonFormat = Utils.loadSettingsFromSharedPreferences();
        Settings settings = gson.fromJson(settingsJsonFormat, Settings.class);

        // set it up in singleton Settings
        if(settings != null) {
            Settings.getInstance().setInFamily(settings.isInFamily());
            Settings.getInstance().setAge(settings.getAge());
            Settings.getInstance().setIsExtrovert(settings.getIsExtrovert());
            Settings.getInstance().setIsOwl(settings.getIsOwl());
            Settings.getInstance().setGoldenSentences(settings.getGoldenSentences());
            Settings.getInstance().setWeekly_target_red_card(settings.getWeekly_target_red_card());
            Settings.getInstance().setWeekly_target_green_card(settings.getWeekly_target_green_card());
            Settings.getInstance().setWeekly_target_blue_card(settings.getWeekly_target_blue_card());

        }

    }

}
