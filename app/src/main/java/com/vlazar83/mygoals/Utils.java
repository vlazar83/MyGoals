package com.vlazar83.mygoals;

import java.util.ArrayList;

public class Utils {

    public static ArrayList<String> getCardGoalList(ArrayList<CardShape> cardShapesList){

        ArrayList<String> cardGoalList = new ArrayList<String>();

        for(CardShape cardShape: cardShapesList){
            cardGoalList.add(cardShape.getCardGoal());
        }

        return cardGoalList;

    }
}
