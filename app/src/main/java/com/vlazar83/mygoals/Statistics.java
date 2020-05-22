package com.vlazar83.mygoals;

public interface Statistics {

    int getLightGreenCardCount();
    int getGreenCardCount();
    int getBlueCardCount();
    int getRedCardCount();

    void incrementLightGreenCardCount();
    void incrementGreenCardCount();
    void incrementBlueCardCount();
    void incrementRedCardCount();

}

class Day implements Statistics{

    // shows which day we are at
    private int dayOfYear;

    private int lightGreenCardCount;
    private int greenCardCount;
    private int blueCardCount;
    private int redCardCount;

    public Day(int dayOfYear) {
        this.dayOfYear = dayOfYear;
        this.lightGreenCardCount = 0;
        this.greenCardCount = 0;
        this.blueCardCount = 0;
        this.redCardCount = 0;
    }

    @Override
    public int getLightGreenCardCount() {
        return lightGreenCardCount;
    }

    @Override
    public int getGreenCardCount() {
        return greenCardCount;
    }

    @Override
    public int getBlueCardCount() {
        return blueCardCount;
    }

    @Override
    public int getRedCardCount() {
        return redCardCount;
    }

    @Override
    public void incrementLightGreenCardCount() {
        lightGreenCardCount += 1;
    }

    @Override
    public void incrementGreenCardCount() {
        greenCardCount += 1;
    }

    @Override
    public void incrementBlueCardCount() {
        blueCardCount += 1;
    }

    @Override
    public void incrementRedCardCount() {
        redCardCount += 1;
    }
}