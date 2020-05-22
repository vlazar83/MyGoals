package com.vlazar83.mygoals;

import java.util.ArrayList;
import java.util.List;

// Singleton class used to hold the pictures as cards for the card creation flow
public class CreationCardSet {

    private static CreationCardSet uniqueInstance = null;
    private ArrayList<CardShape> cardShapesList;

    private CreationCardSet(){
        cardShapesList = new ArrayList<CardShape>();
        fillWithDefaultSet();
    }

    public static synchronized CreationCardSet getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new CreationCardSet();
        }

        return uniqueInstance;

    }

    private void fillWithDefaultSet(){
        CardFactory cardFactory = new CardFactory();
        addCard(cardFactory.getCardShape("RedCard", "noname", "noname", "https://source.unsplash.com/Xq1ntWruZQI/600x800"));
        addCard(cardFactory.getCardShape("BlueCard", "noname", "noname", "https://source.unsplash.com/NYyCqdBOKwc/600x800"));
        addCard(cardFactory.getCardShape("LightGreenCard", "LightGreenCard example", "noname", "https://source.unsplash.com/USrZRcRS2Lw/600x800"));
        addCard(cardFactory.getCardShape("GreenCard", "noname", "noname", "https://source.unsplash.com/buF62ewDLcQ/600x800"));
        addCard(cardFactory.getCardShape("RedCard", "noname", "noname", "https://source.unsplash.com/THozNzxEP3g/600x800"));
        addCard(cardFactory.getCardShape("BlueCard", "noname", "noname", "https://source.unsplash.com/NYyCqdBOKwc/600x800"));
    }

    public void addCard(CardShape cardShape){
        cardShapesList.add(cardShape);
    }

    public List<CardShape> getCardShapeList(){
        return cardShapesList;
    }

}
