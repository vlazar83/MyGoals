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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

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
    private static final String LAST_DISPLAYED_AGE_RELATED_MESSAGE_KEY = "LAST_DISPLAYED_AGE_RELATED_MESSAGE_KEY";

    private static String[] defaultAge20RelatedSentences = {"Age 20", "Age 21"};
    private static String[] defaultAge30RelatedSentences = {"Age 30", "Age 31"};
    private static String[] defaultAge40RelatedSentences = {"Age 40", "Age 41"};
    private static String[] defaultAge50RelatedSentences = {"Age 50", "Age 51"};
    private static String[] defaultAge60RelatedSentences = {"Age 60", "Age 61"};

    public static ArrayList<String> getCardGoalList(ArrayList<CardShape> cardShapesList){

        ArrayList<String> cardGoalList = new ArrayList<String>();

        for(CardShape cardShape: cardShapesList){
            cardGoalList.add(cardShape.getCardGoal());
        }

        return cardGoalList;

    }

    public static void scanForHighestCardIds(ArrayList<CardShape> cardShapesList){

        int foundHighestBlueId = -100;
        int foundHighestGreenId = -100;
        int foundHighestLightGreenId = -100;
        int foundHighestRedId = -100;
        if(!cardShapesList.isEmpty()){

            for(int i =0; i<cardShapesList.size();i++){

                if(cardShapesList.get(i).getCardClass().equalsIgnoreCase("RedCard")){
                    if(cardShapesList.get(i).getId() > foundHighestRedId){
                        foundHighestRedId = cardShapesList.get(i).getId();
                    }
                } else if(cardShapesList.get(i).getCardClass().equalsIgnoreCase("GreenCard")){
                    if(cardShapesList.get(i).getId() > foundHighestGreenId){
                        foundHighestGreenId = cardShapesList.get(i).getId();
                    }
                } else if(cardShapesList.get(i).getCardClass().equalsIgnoreCase("LightGreenCard")){
                    if(cardShapesList.get(i).getId() > foundHighestLightGreenId){
                        foundHighestLightGreenId = cardShapesList.get(i).getId();
                    }
                } else if(cardShapesList.get(i).getCardClass().equalsIgnoreCase("BlueCard")){
                    if(cardShapesList.get(i).getId() > foundHighestBlueId){
                        foundHighestBlueId = cardShapesList.get(i).getId();
                    }
                }

            }
        }

        if(BlueCard.getCount() < foundHighestBlueId) BlueCard.setCount(foundHighestBlueId + 1);
        if(RedCard.getCount() < foundHighestRedId) RedCard.setCount(foundHighestRedId + 1);
        if(GreenCard.getCount() < foundHighestGreenId) GreenCard.setCount(foundHighestGreenId + 1);
        if(LightGreenCard.getCount() < foundHighestLightGreenId) LightGreenCard.setCount(foundHighestLightGreenId + 1);

    }



    public static String getRandomAgeRelatedMessage(){

        int age = Settings.getInstance().getAge();
        String selectedMessage = "";

        if(age > 19 && age < 30){
            int sizeOfList = defaultAge20RelatedSentences.length;
            Random rand = new Random();
            int value = rand.nextInt(sizeOfList);
            selectedMessage = defaultAge20RelatedSentences[value];
        } else if (age > 29 && age < 40){
            int sizeOfList = defaultAge30RelatedSentences.length;
            Random rand = new Random();
            int value = rand.nextInt(sizeOfList);
            selectedMessage = defaultAge30RelatedSentences[value];
        } else if (age > 39 && age < 50){
            int sizeOfList = defaultAge40RelatedSentences.length;
            Random rand = new Random();
            int value = rand.nextInt(sizeOfList);
            selectedMessage = defaultAge40RelatedSentences[value];
        } else if (age > 49 && age < 60){
            int sizeOfList = defaultAge50RelatedSentences.length;
            Random rand = new Random();
            int value = rand.nextInt(sizeOfList);
            selectedMessage = defaultAge50RelatedSentences[value];
        } else if (age > 60){
            int sizeOfList = defaultAge60RelatedSentences.length;
            Random rand = new Random();
            int value = rand.nextInt(sizeOfList);
            selectedMessage = defaultAge60RelatedSentences[value];
        }

        return selectedMessage;

    }

    public static void saveDayAboutLastDisplayedAgeRelatedMessageToSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(LAST_DISPLAYED_AGE_RELATED_MESSAGE_KEY, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

        Date date = new Date(System.currentTimeMillis());
        long millis = date.getTime();

        edit.putLong(LAST_DISPLAYED_AGE_RELATED_MESSAGE_KEY, millis);
        edit.apply();

    }

    public static Date loadDayAboutLastDisplayedAgeRelatedMessageFromSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(LAST_DISPLAYED_AGE_RELATED_MESSAGE_KEY, MODE_PRIVATE);

        Date futureDate = new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();

        long myDate = preferences.getLong(LAST_DISPLAYED_AGE_RELATED_MESSAGE_KEY, futureDate.getTime());

        return new Date(myDate);

    }

    public static boolean checkIfAgeRelatedMessageDisplayIsNeeded(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(loadDayAboutLastDisplayedAgeRelatedMessageFromSharedPreferences());
        cal.add(Calendar.HOUR_OF_DAY, 168);  // calculate a one week later time

        if(new Date(System.currentTimeMillis()).after(cal.getTime())){
            return true;
        } else {
            return false;
        }

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

    public static String getLightGreenCardsCountFromWeek(StatisticsHolder statisticsHolder){
        int total=0;

        for(int i=getFirstDayOfWeek();i<getLastDayOfWeek();i++){
            total += statisticsHolder.getStatistic(i).getLightGreenCardCount();
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

    public static void scheduleAlarmOnSpecifiedDay(int dayOfWeek, int hour, int minute, Class classOfBroadcast){

        Intent intent = new Intent(MyGoals.getAppContext(), classOfBroadcast);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MyGoals.getAppContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) MyGoals.getAppContext().getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),7 * 24 * 60 * 60 * 1000,pendingIntent);

    }

    public static void cancelAllAlarms(){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyGoals.getAppContext());
        notificationManager.cancelAll();

        AlarmManager alarmManager = (AlarmManager) MyGoals.getAppContext().getSystemService(Context.ALARM_SERVICE);


        Intent alarmIntent = new Intent(MyGoals.getAppContext(), ReminderBroadcast.class);

        PendingIntent displayIntent = PendingIntent.getBroadcast(MyGoals.getAppContext(), 0, alarmIntent, 0);

        alarmManager.cancel(displayIntent);

        alarmIntent = new Intent(MyGoals.getAppContext(), ReminderBroadcastForIntroExtro.class);

        displayIntent = PendingIntent.getBroadcast(MyGoals.getAppContext(), 0, alarmIntent, 0);

        alarmManager.cancel(displayIntent);

        alarmIntent = new Intent(MyGoals.getAppContext(), ReminderBroadcastForOwlOrLark.class);

        displayIntent = PendingIntent.getBroadcast(MyGoals.getAppContext(), 0, alarmIntent, 0);

        alarmManager.cancel(displayIntent);

    }

}
