package com.vlazar83.mygoals;

import java.util.ArrayList;
import java.util.List;

public class DefaultCardSet {

    private static DefaultCardSet uniqueInstance = null;
    private ArrayList<CardShape> cardShapesList;

    private DefaultCardSet(){
        cardShapesList = new ArrayList<CardShape>();
        fillWithDefaultSet();
    }

    public static synchronized DefaultCardSet getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new DefaultCardSet();
        }

        return uniqueInstance;

    }

    private void fillWithDefaultSet(){
        CardFactory cardFactory = new CardFactory();
        addCard(cardFactory.getCardShape("RedCard", "put the trash out"));
        addCard(cardFactory.getCardShape("BlueCard", "put the trash out2"));
        addCard(cardFactory.getCardShape("GreenCard", "put the trash out3"));
        addCard(cardFactory.getCardShape("RedCard", "put the trash out4"));
        addCard(cardFactory.getCardShape("GreenCard", "put the trash out5"));
    }

    public void addCard(CardShape cardShape){
        cardShapesList.add(cardShape);
    }

    public List<CardShape> getCardShapeList(){
        return cardShapesList;
    }

}
