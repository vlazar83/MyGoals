package com.vlazar83.mygoals;

public class CardFactory {

    public CardShape getCardShape(String cardShapeType, String cardGoal){

        if(cardShapeType == null){
            return null;
        } else if (cardShapeType.equalsIgnoreCase("BlueCard")){
            return new BlueCard(cardGoal);
        } else if (cardShapeType.equalsIgnoreCase("RedCard")){
            return new RedCard(cardGoal);
        }

        return null;

    }
}
