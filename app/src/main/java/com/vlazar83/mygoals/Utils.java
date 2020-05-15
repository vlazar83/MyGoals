package com.vlazar83.mygoals;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class Utils {

    private static final String ACTUAL_CARDS = "ACTUAL_CARDS";
    private static final String LEADING_IDEA = "LEADING_IDEA";
    private static final String STATISTICS = "STATISTICS";
    private static final String STATISTICS_YEAR = "STATISTICS_YEAR";
    public static final int STATISTICS_YEAR_DEFAULT = 1990;
    private static final String CREATED_CARDS = "CREATED_CARDS";
    private static final String SETTINGS = "SETTINGS";

    public static ArrayList<String> getCardGoalList(ArrayList<CardShape> cardShapesList){

        ArrayList<String> cardGoalList = new ArrayList<String>();

        for(CardShape cardShape: cardShapesList){
            cardGoalList.add(cardShape.getCardGoal());
        }

        return cardGoalList;

    }

    public static void saveSharedPreferences(String data){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(ACTUAL_CARDS, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(ACTUAL_CARDS, data);
        edit.apply();

    }

    public static void saveLeadingIdeaToSharedPreferences(String data){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(LEADING_IDEA, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(LEADING_IDEA, data);
        edit.apply();

    }

    public static String loadLeadingIdeaFromSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(LEADING_IDEA, MODE_PRIVATE);
        String leadingIdea = preferences.getString(LEADING_IDEA, "");

        return leadingIdea;

    }

    public static String loadSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(ACTUAL_CARDS, MODE_PRIVATE);
        String cardsJsonFormat = preferences.getString(ACTUAL_CARDS, "");

        return cardsJsonFormat;

    }

    public static void saveStatisticsToSharedPreferences(String data){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(STATISTICS, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(STATISTICS, data);
        edit.apply();

        saveStatisticsYearToSharedPreferences();

    }

    public static String loadStatisticsFromSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(STATISTICS, MODE_PRIVATE);
        String cardsJsonFormat = preferences.getString(STATISTICS, "");

        return cardsJsonFormat;

    }

    public static void saveStatisticsYearToSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(STATISTICS_YEAR, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(STATISTICS_YEAR, StatisticsHolder.getInstance().getYear());
        edit.apply();

    }

    public static int loadStatisticsYearFromSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(STATISTICS_YEAR, MODE_PRIVATE);
        int statisticsYear = preferences.getInt(STATISTICS_YEAR, STATISTICS_YEAR_DEFAULT);

        return statisticsYear;

    }

    public static void saveCreatedCardsToSharedPreferences(String data){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(CREATED_CARDS, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(CREATED_CARDS, data);
        edit.apply();

    }

    public static String loadCreatedCardsFromSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(CREATED_CARDS, MODE_PRIVATE);
        String cardsJsonFormat = preferences.getString(CREATED_CARDS, "");

        return cardsJsonFormat;

    }

    public static void loadCreatedCardsFromSharedPreferencesNew(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(CREATED_CARDS, MODE_PRIVATE);
        String cardsJsonFormat = preferences.getString(CREATED_CARDS, "");
        if(!cardsJsonFormat.equalsIgnoreCase("[]") && !cardsJsonFormat.equalsIgnoreCase("") ){
            //Create our gson instance
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(CardShape.class, new InterfaceAdapter());
            Gson gson = builder.create();

            Type typeOfSrc = new TypeToken<Collection<CardShape>>(){}.getType();
            ArrayList<CardShape> cardJsonArray = gson.fromJson(cardsJsonFormat, typeOfSrc);

            CreatedCardSet.getInstance().setCardShapeList(cardJsonArray);

        }
    }

    public static void saveSettingsToSharedPreferences(String data){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(SETTINGS, data);
        edit.apply();

    }

    public static String loadSettingsFromSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(SETTINGS, MODE_PRIVATE);
        String settingsJsonFormat = preferences.getString(SETTINGS, "");

        return settingsJsonFormat;

    }

    // to calculate the begin day of the year based on today
    public static int getFirstDayOfWeek(){
        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        Calendar myDate = Calendar.getInstance();

        // Sunday = 1 !! Therefore we switch it to 7.
        int dayOfWeek = myDate.get (Calendar.DAY_OF_WEEK);

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeek = 7;
                break;
            default:
                dayOfWeek -= 1;
                break;

        }

        // if it would go negative, it means we are at the beginning of the year, in the first week, therefore return 1.
        if( today - dayOfWeek + 1 <= 0 ) {
            return 1;
        } else {
            return today - dayOfWeek + 1;
        }
    }

    // to calculate the end day of the year based on today
    public static int getLastDayOfWeek(){
        int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        Calendar myDate = Calendar.getInstance();
        int dayOfWeek = myDate.get (Calendar.DAY_OF_WEEK);

        return today + 7 - dayOfWeek +1;
    }

    public static String getRedCardsCountFromWeek(StatisticsHolder statisticsHolder){
        int total=0;

        for(int i=getFirstDayOfWeek();i<getLastDayOfWeek();i++){
            total += statisticsHolder.getStatistic(i).getRedCardCount();
        }
        return String.valueOf(total);
    }

    public static String getGreenCardsCountFromWeek(StatisticsHolder statisticsHolder){
        int total=0;

        for(int i=getFirstDayOfWeek();i<getLastDayOfWeek();i++){
            total += statisticsHolder.getStatistic(i).getGreenCardCount();
        }
        return String.valueOf(total);
    }

    public static String getBlueCardsCountFromWeek(StatisticsHolder statisticsHolder){
        int total=0;
        int end=getLastDayOfWeek();

        for(int i=getFirstDayOfWeek();i<end;i++){
            total += statisticsHolder.getStatistic(i).getBlueCardCount();
        }
        return String.valueOf(total);
    }

    public static void scheduleAlarm(int hour, int minute, long interval, Class classOfBroadcast){

        Intent intent = new Intent(MyGoals.getAppContext(), classOfBroadcast);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MyGoals.getAppContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) MyGoals.getAppContext().getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval,pendingIntent);

    }

    public static void cancelAllAlarms(){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyGoals.getAppContext());
        notificationManager.cancelAll();
    }

}
