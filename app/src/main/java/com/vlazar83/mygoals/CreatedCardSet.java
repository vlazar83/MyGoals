package com.vlazar83.mygoals;

import java.util.ArrayList;
import java.util.List;

// Singleton class to hold all the manually created cards
public class CreatedCardSet {

    private static CreatedCardSet uniqueInstance = null;
    private List<CardShape> cardShapesList;

    private CreatedCardSet(){
        cardShapesList = new ArrayList<CardShape>();
    }

    public static synchronized CreatedCardSet getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new CreatedCardSet();
        }

        return uniqueInstance;

    }

    public void addCard(CardShape cardShape){
        cardShapesList.add(cardShape);
    }

    public void setCardShapeList(List<CardShape> list){
        this.cardShapesList = list;
    }

    public List<CardShape> getCardShapeList(){
        return cardShapesList;
    }

    public void emptyList(){ cardShapesList.clear();}

}
