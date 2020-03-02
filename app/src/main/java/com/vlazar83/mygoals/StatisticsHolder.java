package com.vlazar83.mygoals;

import java.util.ArrayList;
import java.util.Calendar;

//Singleton class
public class StatisticsHolder {

    // To keep track about which year we are collecting data.
    // After each year we drop the earlier and start a new collection.
    private static int year;

    private static StatisticsHolder uniqueInstance = null;
    private ArrayList<Statistics> statisticsList;

    private StatisticsHolder(){
        statisticsList = new ArrayList<Statistics>();
        year = Calendar.getInstance().get(Calendar.YEAR);
    }

    public static synchronized StatisticsHolder getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new StatisticsHolder();
        }

        return uniqueInstance;

    }

    public ArrayList<Statistics> getStatisticsList(){
        return statisticsList;
    }

    public void addStatistic(Statistics statistic){
        statisticsList.add(statistic);
    }

    public Statistics getStatistic(int day){
        return statisticsList.get(day);
    }

    public void removeStatistic(int day){
        statisticsList.remove(day);
    }

}
