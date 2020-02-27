package com.vlazar83.mygoals;

public class CardFactory {

    public CardShape getCardShape(String cardShapeType, String cardGoal, String city, String url){

        if(cardShapeType == null){
            return null;
        } else if (cardShapeType.equalsIgnoreCase("BlueCard")){
            return new BlueCard(cardGoal, city, url);
        } else if (cardShapeType.equalsIgnoreCase("RedCard")){
            return new RedCard(cardGoal, city, url);
        } else if (cardShapeType.equalsIgnoreCase("GreenCard")){
            return new GreenCard(cardGoal, city, url);
        }

        return null;

    }
}
