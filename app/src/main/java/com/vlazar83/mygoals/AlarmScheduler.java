package com.vlazar83.mygoals;

public class AlarmScheduler {

    static void scheduleAllAlarms(){

        if(Settings.getInstance().getIsOwl()){
            Utils.scheduleAlarm(19,15,86400000, ReminderBroadcast.class);
            Utils.scheduleAlarmOnSpecifiedDay(7,18,15, ReminderBroadcastForIntroExtro.class);
            Utils.scheduleAlarmOnSpecifiedDay(5,18,15, ReminderBroadcastForOwlOrLark.class);

        } else {
            Utils.scheduleAlarm(10,15,86400000, ReminderBroadcast.class);
            Utils.scheduleAlarmOnSpecifiedDay(7,9,15, ReminderBroadcastForIntroExtro.class);
            Utils.scheduleAlarmOnSpecifiedDay(5,9,15, ReminderBroadcastForOwlOrLark.class);
        }

    }

}
