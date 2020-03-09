package com.vlazar83.mygoals;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    private static final String ACTUAL_CARDS = "ACTUAL_CARDS";
    private static final String STATISTICS = "STATISTICS";

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

    }

    public static String loadStatisticsFromSharedPreferences(){
        Context context = MyGoals.getAppContext();
        SharedPreferences preferences =  context.getSharedPreferences(STATISTICS, MODE_PRIVATE);
        String cardsJsonFormat = preferences.getString(STATISTICS, "");

        return cardsJsonFormat;

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

}
