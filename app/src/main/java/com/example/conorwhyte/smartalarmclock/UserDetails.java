package com.example.conorwhyte.smartalarmclock;

import android.location.Location;

/**
 * Created by conorwhyte on 14/11/2016.
 */

public class UserDetails {

    //Possible global variables needed from the user
    public long punisherContact ;
    public String punisherMessage ;

    public int[] prepTime ;
    public int totalRepTime ;
    public Location destination ;
    public int journeyTime ;

    public int wakTimeHour ;
    public int wakeTimeMinute ;

    public String[] cardName ;
    public String[] cardDescription;
    public int[] cardWakeHour;
    public int[] cardWakeNumber ;

    public String alarmType ;
    public void chooseAlarmType(int chooseAlarm){
        switch (chooseAlarm){
            case 0:
                alarmType = "normal";
                break;
            case 1:
                alarmType = "movement";
                break;
            case 2:
                alarmType = "puzzle";
                break;
            case 3:
                alarmType = "punisher";
                break;
            default :
                alarmType = "normal";
                break;
        }
    }

}
