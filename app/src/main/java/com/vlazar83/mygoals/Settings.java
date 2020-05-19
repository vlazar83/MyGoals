package com.vlazar83.mygoals;

import java.util.ArrayList;
import java.util.Random;

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

    public void setGoldenSentences(ArrayList<String> goldenSentences){

        this.goldenSentences.clear();
        this.goldenSentences.addAll(goldenSentences);

    }


    public void dropGoldenSentence(int index){

        this.goldenSentences.remove(index);

    }

    public void addGoldenSentence(String newSentence){
        this.goldenSentences.add(0,newSentence);
    }

    public ArrayList<String> getGoldenSentences(){
        return this.goldenSentences;
    }

    public String getGoldenSentence(int index){
        return this.goldenSentences.get(index);
    }

    public String getRandomGoldenSentence(){
        String randomSentence = "";
        if(goldenSentences.isEmpty()){
            randomSentence = MyGoals.getAppContext().getString(R.string.GoldenSentencesAreEmpty);
        } else {
            Random rand = new Random();
            int value = rand.nextInt(goldenSentences.size());
            if(value >=0 && value < goldenSentences.size()){
                randomSentence = goldenSentences.get(value);
            }
        }
        return randomSentence;
    }
}
