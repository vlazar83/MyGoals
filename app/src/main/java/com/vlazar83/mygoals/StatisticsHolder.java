package com.vlazar83.mygoals;

import java.util.ArrayList;
import java.util.Calendar;

//Singleton class
public class StatisticsHolder {

    // To keep track about which year we are collecting data.
    // After each year we drop the earlier and start a new collection. This is done in MainActivity in setupStatistics() .
    private static int year;

    private static StatisticsHolder uniqueInstance = null;
    private ArrayList<Statistics> statisticsList;

    private StatisticsHolder(){
        //create the array and initialize it.
        statisticsList = new ArrayList<>();
        for(int i = 0; i < 367; i++) {
            statisticsList.add(i,new Day(i));
        }
        year = Calendar.getInstance().get(Calendar.YEAR);
    }

    public static synchronized StatisticsHolder getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new StatisticsHolder();
        }

        return uniqueInstance;

    }

    public void setStatisticsArray(ArrayList<Statistics> statistics){
        this.statisticsList = statistics;
    }

    public ArrayList<Statistics> getStatisticsList(){
        return statisticsList;
    }

    public void addStatistic(Statistics statistic, int position){
        statisticsList.remove(position);
        statisticsList.add(position, statistic);
    }

    public Statistics getStatistic(int day){
        return statisticsList.get(day);
    }

    public int getYear(){
        return this.year;
    }

    public static void resetStatisticsHolder(){

        uniqueInstance = null;

    }

}
