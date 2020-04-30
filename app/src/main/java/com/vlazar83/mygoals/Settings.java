package com.vlazar83.mygoals;

import java.util.ArrayList;

public class Settings {

    private static Settings uniqueInstance = null;
    public static final int default_age = 40;
    public static final int max_age = 100;
    public static final int min_age = 18;
    private boolean inFamily;
    private int age;
    private boolean isExtrovert;
    private boolean isOwl;
    private ArrayList<String> goldenSentences;

    private Settings() {
        this.inFamily = true;
        this.age = 20;
        this.isExtrovert = true;
        this.isOwl = true;
        this.goldenSentences = new ArrayList<String>();
        setDefaultGoldenSentences();
    }

    public static synchronized Settings getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new Settings();
        }

        return uniqueInstance;

    }

    public boolean isInFamily() {
        return inFamily;
    }

    public void setInFamily(boolean inFamily) {
        this.inFamily = inFamily;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if(age <= min_age) {
            this.age = default_age;
        } else if(age >= max_age) {
            this.age = default_age;
        } else {
            this.age = age;
        }
    }

    public void setIsExtrovert(boolean status){
        this.isExtrovert = status;
    }

    public boolean getIsExtrovert(){
        return this.isExtrovert;
    }

    public void setIsOwl(boolean status){
        this.isOwl = status;
    }

    public boolean getIsOwl(){
        return this.isOwl;
    }

    private void setDefaultGoldenSentences(){

        this.goldenSentences.add("Sok lud disznot gyöz");
        this.goldenSentences.add("Ki koran kel aranyat lel");
        this.goldenSentences.add("TBDL...");
        this.goldenSentences.add("TBDL...");
        this.goldenSentences.add("TBDL...");
        this.goldenSentences.add("TBDL...");
        this.goldenSentences.add("TBDL...");
        this.goldenSentences.add("TBDL...");
        this.goldenSentences.add("TBDL...");
        this.goldenSentences.add("TBDL...");

    }

    public void addGoldenSentence(String newSentence){
        this.goldenSentences.add(newSentence);
    }

    public ArrayList<String> getGoldenSentences(){
        return this.goldenSentences;
    }

}
