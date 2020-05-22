package com.vlazar83.mygoals;

public interface CardShape {

    void draw();
    String getCardGoal();
    String getCardCity();
    String getCardUrl();
    int getId();
    String getCardClass();

}

// represents cards for Family activities
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

    public String getCardClass(){
        return "BlueCard";
    }

    @Override
    public void draw() {
        System.out.println("Inside BlueCard draw() method");
    }

}

// represents cards for Work activities
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

    public String getCardClass(){
        return "RedCard";
    }

    @Override
    public void draw() {
        System.out.println("Inside RedCard draw() method");
    }

}

// represents cards for Private time (alone) activities - for an introvert type of person
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

    public String getCardClass(){
        return "GreenCard";
    }

    @Override
    public void draw() {
        System.out.println("Inside GreenCard draw() method");
    }

}

// represents cards for Private time (with friends) activities - for an extrovert type of person
// in the statistics we calculate this with GreenCard together
class LightGreenCard implements CardShape{

    private static int count;
    private int id;
    private String cardGoal;
    private String city;
    private String url;

    public LightGreenCard(String cardGoal, String city, String url) {
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

    public String getCardClass(){
        return "LightGreenCard";
    }

    @Override
    public void draw() {
        System.out.println("Inside LightGreenCard draw() method");
    }

}