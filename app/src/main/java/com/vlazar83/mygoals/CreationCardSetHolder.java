package com.vlazar83.mygoals;

import java.util.ArrayList;

//Singleton class
public class CreationCardSetHolder {

    private static CreationCardSetHolder uniqueInstance = null;
    private ArrayList<CardShape> cardShapesList;

    private CreationCardSetHolder(){
        cardShapesList = new ArrayList<CardShape>();
        CreationCardSet creationCardSet = CreationCardSet.getInstance();
        cardShapesList.addAll(creationCardSet.getCardShapeList());
    }

    public static synchronized CreationCardSetHolder getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new CreationCardSetHolder();
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
