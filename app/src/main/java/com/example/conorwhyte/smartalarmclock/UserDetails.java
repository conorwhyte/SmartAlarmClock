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

    private boolean firstTime = true ;

    private ArrayList<String> cardNames = new ArrayList<String>();
    private ArrayList<Integer> cardTimes = new ArrayList<Integer>();
    private Location home;
    private Location destination;
    private int journeyTime;
    private int arrivalHour;
    private int arrivalMinute;
    private int alarmHour = 9;
    private int alarmMinute = 59;

    // add / get cards
    public void addCard(String name)
        {this.cardNames.add(name);}
    public ArrayList<String> getCardNames()
        {return this.cardNames;}

    // remove card
    public void removeCard(String name)
        {int i = getCardNames().indexOf(name);
            this.cardNames.remove(i);
        }

    // add / get times
    public void addTime(Integer time)
        {this.cardTimes.add(time);}
    public ArrayList<Integer> getCardTimes()
        {return this.cardTimes;}

    // set / get home location
    public void setHome(Location home)
        {this.home = home;}
    public Location getHome()
        {return this.home;}

    // set / get destination location
    public void setDestination(Location dest)
        {this.destination = dest;}
    public Location getDestination()
        {return this.destination;}

    // get card count / get total time of all cards
    public int getCardCount()
        {if(this.cardNames.isEmpty()){return 0;}
            return getCardNames().size();}
    public int getTotalCardTimes()
        {int total = 0;
         for(int i = 0; i < getCardTimes().size(); i++)
            {total += getCardTimes().get(i);}
         return total;
        }

    // set first time true or false
    public void setFirstTime(boolean set)
        {this.firstTime = set ;}
    public boolean getFirstTime()
        {return this.firstTime;}

    // set / get journey time
    public void setJourneyTime(int time)
        {this.journeyTime = time;}
    public int getJourneyTime()
        {return this.journeyTime;}

    // get total time of journey and cards
    public int getTotalTime()
        {return getJourneyTime() + getTotalCardTimes();}

    public void setArrivalHour(Integer h)
        {this.arrivalHour = h;}
    public int getArrivalHour()
        {return this.arrivalHour;}
    public void setArrivalMin(Integer m)
        {this.arrivalMinute = m;}
    public int getArrivalMin()
        {return this.arrivalMinute;}

    // set / get alarm hour
    public void setAlarmHour(Integer h)
        {this.alarmHour = h;}
    public int getAlarmHour()
        {return this.alarmHour;}

    // set / get alarm minute
    public void setAlarmMinute(Integer m)
        {this.alarmMinute = m;}
    public int getAlarmMin()
        {return this.alarmMinute - getTotalTime();}
}
