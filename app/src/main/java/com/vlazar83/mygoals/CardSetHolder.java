package com.vlazar83.mygoals;

import java.util.ArrayList;
import java.util.List;

//Singleton class
public class CardSetHolder {

    private static CardSetHolder uniqueInstance = null;
    private ArrayList<CardShape> cardShapesList;

    private CardSetHolder(){
        cardShapesList = new ArrayList<CardShape>();
        DefaultCardSet defaultCardSet = DefaultCardSet.getInstance();
        cardShapesList.addAll(defaultCardSet.getCardShapeList());
    }

    public static synchronized CardSetHolder getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new CardSetHolder();
        }

        return uniqueInstance;

    }

    public void addCard(CardShape cardShape){
        cardShapesList.add(cardShape);
    }

    public void removeCard(CardShape cardShape){
        cardShapesList.remove(cardShape);
    }

    public void setupCardList(ArrayList<CardShape> cardShapeList){
        cardShapesList = cardShapeList;
    }

    public void emptyCardList(){
        cardShapesList = new ArrayList<CardShape>();
    }

    public ArrayList<CardShape> getCardShapeList(){
        return cardShapesList;
    }

    public int getListSize(){
        return cardShapesList.size();
    }

}
