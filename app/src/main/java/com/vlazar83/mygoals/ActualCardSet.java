package com.vlazar83.mygoals;

import java.util.ArrayList;
import java.util.List;

public class ActualCardSet {

    private static ActualCardSet uniqueInstance = null;
    private ArrayList<CardShape> cardShapesList;

    private ActualCardSet(){
        cardShapesList = new ArrayList<CardShape>();
    }

    public static synchronized ActualCardSet getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new ActualCardSet();
        }

        return uniqueInstance;

    }

    public void addCard(CardShape cardShape){
        cardShapesList.add(cardShape);
    }

    public List<CardShape> getCardShapeList(){
        return cardShapesList;
    }

}
