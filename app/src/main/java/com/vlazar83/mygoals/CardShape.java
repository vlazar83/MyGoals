package com.vlazar83.mygoals;

public interface CardShape {

    void draw();
    String getCardGoal();

}

class BlueCard implements CardShape{

    private String cardGoal;

    public BlueCard(String cardGoal) {
        this.cardGoal = cardGoal;
    }

    public String getCardGoal() {
        return cardGoal;
    }

    public void setCardGoal(String cardGoal) {
        this.cardGoal = cardGoal;
    }

    @Override
    public void draw() {
        System.out.println("Inside BlueCard draw() method");
    }

}

class RedCard implements CardShape{

    private String cardGoal;

    public RedCard(String cardGoal) {
        this.cardGoal = cardGoal;
    }

    public String getCardGoal() {
        return cardGoal;
    }

    public void setCardGoal(String cardGoal) {
        this.cardGoal = cardGoal;
    }

    @Override
    public void draw() {
        System.out.println("Inside RedCard draw() method");
    }

}

class GreenCard implements CardShape{

    private String cardGoal;

    public GreenCard(String cardGoal) {
        this.cardGoal = cardGoal;
    }

    public String getCardGoal() {
        return cardGoal;
    }

    public void setCardGoal(String cardGoal) {
        this.cardGoal = cardGoal;
    }

    @Override
    public void draw() {
        System.out.println("Inside GreenCard draw() method");
    }

}