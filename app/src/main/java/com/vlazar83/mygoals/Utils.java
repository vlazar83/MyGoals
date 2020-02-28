package com.vlazar83.mygoals;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    private static final String ACTUAL_CARDS = "ACTUAL_CARDS";

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

}
