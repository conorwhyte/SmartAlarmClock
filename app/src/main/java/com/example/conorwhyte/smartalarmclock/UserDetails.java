package com.example.conorwhyte.smartalarmclock;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by conorwhyte on 14/11/2016.
 */

public class UserDetails implements Serializable{

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
    public ArrayList<String> cardNa;
    public String[] cardDescription;
    public int[] cardWakeHour;
    public int[] cardWakeTime;
    public ArrayList<Integer> cardTime ;

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

    public void setCardWakeTime(ArrayList<Integer> time) {
        cardTime = time;
    }

    public void setCardName(ArrayList<String> name) {
        cardNa = name;
    }

    public String getCardName(int i){
        String name = cardNa.get(i);
        return name ;
    }

    public int getCardTime(int i){
        if(cardTime.isEmpty()){
            return 0 ;
        }
        else{
            int time = cardTime.get(i);
            return time ;
        }
    }
}
