package com.vlazar83.mygoals;

public interface CardShape {

    void draw();
    String getCardGoal();
    String getCardCity();
    String getCardUrl();
    int getId();

}

class BlueCard implements CardShape{

    private static int count;
    private int id;
    private String cardGoal;
    private String city;
    private String url;

    public BlueCard(String cardGoal, String city, String url) {
        this.cardGoal = cardGoal;
        this.city = city;
        this.url = url;
        id=count;
        count = count+1;
    }

    public String getCardGoal() {
        return cardGoal;
    }

    public String getCardCity() {
        return city;
    }

    public String getCardUrl() {
        return url;
    }

    public int getId(){ return id;}

    public void setCardGoal(String cardGoal) {
        this.cardGoal = cardGoal;
    }

    @Override
    public void draw() {
        System.out.println("Inside BlueCard draw() method");
    }

}

class RedCard implements CardShape{

    private static int count;
    private int id;
    private String cardGoal;
    private String city;
    private String url;

    public RedCard(String cardGoal, String city, String url) {
        this.cardGoal = cardGoal;
        this.city = city;
        this.url = url;
        id=count;
        count = count+1;
    }

    public String getCardGoal() { return cardGoal; }

    public String getCardCity() {
        return city;
    }

    public String getCardUrl() {
        return url;
    }

    public int getId(){ return id;}

    public void setCardGoal(String cardGoal) {
        this.cardGoal = cardGoal;
    }

    @Override
    public void draw() {
        System.out.println("Inside RedCard draw() method");
    }

}

class GreenCard implements CardShape{

    private static int count;
    private int id;
    private String cardGoal;
    private String city;
    private String url;

    public GreenCard(String cardGoal, String city, String url) {
        this.cardGoal = cardGoal;
        this.city = city;
        this.url = url;
        id=count;
        count = count+1;
    }

    public String getCardGoal() {
        return cardGoal;
    }

    public String getCardCity() {
        return city;
    }

    public String getCardUrl() {
        return url;
    }

    public int getId(){ return id;}

    public void setCardGoal(String cardGoal) {
        this.cardGoal = cardGoal;
    }

    @Override
    public void draw() {
        System.out.println("Inside GreenCard draw() method");
    }

}