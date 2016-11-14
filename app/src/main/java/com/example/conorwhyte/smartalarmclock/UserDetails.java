package com.example.conorwhyte.smartalarmclock;

/**
 * Created by conorwhyte on 14/11/2016.
 */

public class UserDetails {
    public long punisherContact ;

    public String alarmType ;

    public void chooseAlarmType(int chooseAlarm){
        switch (chooseAlarm){
            case 0:
                alarmType = "normal";

            case 1:
                alarmType = "movement";

            case 2:
                alarmType = "puzzle";

            case 3:
                alarmType = "punisher";

            default :
                alarmType = "normal";

        }
    }
}
