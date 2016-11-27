package com.example.conorwhyte.smartalarmclock;

import android.location.Location;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class stores all the user details needed for the application. It is an object which will be
 * used in almost every activity, as it stores card times, travel time etc. It has methods to retreive
 * and set certain information.
 *
 * Created by conorwhyte on 14/11/2016.
 */

public class UserDetails implements Serializable{

    //Possible global variables needed from the user
    public long punisherContact ;
    public String punisherMessage ;

    public boolean firstTime = true ;

    public int[] prepTime ;
    public int totalRepTime ;
    public Location destination ;
    public int journeyTime ;

    public int wakTimeHour ;
    public int wakeTimeMinute ;

    int count = 0 ;

    public ArrayList<String> cardName;
    public ArrayList<Integer> cardTime ;

    public void setFirstTime(){
        firstTime = false ;
    }

    public void addCount(){
        count++;
    }


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
        cardName = name;
    }

    public String getCardName(int i){
        String name = cardName.get(i);
        return name ;
    }

    public int getCardTime(int i){
        if(cardTime == null){
            return 0 ;
        }
        else{
            int time = cardTime.get(i);
            return time ;
        }
    }

    public void setNumber(int i ){
        count = i ;
    }

    public int numberOfCards(){
        return count;
    }
}
