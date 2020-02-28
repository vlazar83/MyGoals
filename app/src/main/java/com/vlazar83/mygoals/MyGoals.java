package com.vlazar83.mygoals;

import android.app.Application;
import android.content.Context;

public class MyGoals extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyGoals.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyGoals.context;
    }
}