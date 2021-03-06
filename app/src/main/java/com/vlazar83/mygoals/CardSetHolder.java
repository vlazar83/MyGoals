package com.vlazar83.mygoals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//Singleton class
public class CardSetHolder {

    private static CardSetHolder uniqueInstance = null;
    private List<CardShape> cardShapesList;

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

    public void addCardToPosition(CardShape cardShape, int position){
        cardShapesList.add(position, cardShape);
    }

    public void removeCard(CardShape cardShape){
        cardShapesList.remove(cardShape);
    }

    public void setupCardList(List<CardShape> cardShapeList){
        cardShapesList = cardShapeList;
    }

    public void emptyCardList(){
        cardShapesList = new ArrayList<CardShape>();
    }

    public List<CardShape> getCardShapeList(){
        return cardShapesList;
    }

    public int getListSize(){
        return cardShapesList.size();
    }

    public boolean hasCard(CardShape cardShape){
        // return cardShapesList.contains(cardShape); - this was not working as expected.

        boolean result = false;
        Iterator<CardShape> iterator = cardShapesList.iterator();
        while(iterator.hasNext()){
            CardShape next = iterator.next();
            if(next.getCardClass().equalsIgnoreCase(cardShape.getCardClass()) && next.getId() == cardShape.getId()){
                result = true;
                break;

            }
        }

        return result;

    }

    public void reverseCardShapeList(){
        Collections.reverse(cardShapesList);
    }

}
