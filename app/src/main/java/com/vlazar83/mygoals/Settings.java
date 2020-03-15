package com.vlazar83.mygoals;

public class Settings {

    private static Settings uniqueInstance = null;
    public static final int default_age = 40;
    public static final int max_age = 100;
    public static final int min_age = 18;
    private boolean male;
    private int age;

    private Settings() {
        this.male = true;
        this.age = 20;
    }

    public static synchronized Settings getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new Settings();
        }

        return uniqueInstance;

    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
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
}
