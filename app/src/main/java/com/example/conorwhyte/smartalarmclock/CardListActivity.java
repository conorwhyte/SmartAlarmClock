package com.example.conorwhyte.smartalarmclock;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the activity where the card list will be displayed, each card is added in depending on
 * which ones the user entered on inital setup, and can be edited in the menu at any time. Its also in
 * this activity where we set off each other individual alarm to signify its time to start a new activity
 * during the morning.
 *
 * Created with the help of following tutorial:
 * http://javapapers.com/android/android-cards-list-view/
 *
 * Author: Conor Whyte
 */

public class CardListActivity extends Activity {

    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    boolean inActivity = true ;

    UserDetails user ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = (ListView) findViewById(R.id.card_listView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (UserDetails)extras.getSerializable("Object");
        }

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);

        for (int i = 0; i < 10; i++) {
            //Card card = new Card("Card " + (i+1) + " Line 1", "Card " + (i+1) + " Line 2");
            //cardArrayAdapter.add(card);
        }
        final int[] images = {R.drawable.breakfast, R.drawable.shower, R.drawable.suit, R.drawable.food, R.drawable.car};

        if(user != null){
            for (int i = 0; i < user.getCardCount(); i++) {

                String name1 = user.getCardNames().get(i);
                String time1 = Integer.toString(user.getCardTimes().get(i));
                Card card = new Card(name1, time1, images[1]);
                cardArrayAdapter.add(card);
            }
        }



        /*
        //String sec = Long.toString(secondsRemaining[0]);
        final Card cardShower = new Card("Shower" , "8:05am", images[1] );
        cardArrayAdapter.add(cardShower);

        Card cardDress = new Card("Get Dressed" , "8:15am", images[2]);
        cardArrayAdapter.add(cardDress);

        final Card cardBreak = new Card("Eat Breakfast" , "8:25am", images[0]);
        cardArrayAdapter.add(cardBreak);

        Card cardLunch = new Card("Prepare Lunch" , "8:30am", images[3]);
        cardArrayAdapter.add(cardLunch);

        Card cardLeave = new Card("Leave for Work" , "8:35am", images[4]);
        cardArrayAdapter.add(cardLeave);
        */

        listView.setAdapter(cardArrayAdapter);

        startTimer();

    }

    //Timer methods used to indicate when the alarm should go off next
    public void startTimer() {
        //timerTextView = (TextView) findViewById(R.id.textView2);
        //startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    long startTime = 0;
    int localTimer ;
    long localSecond ;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            localSecond = seconds ;
            localTimer = minutes ;
            timerHandler.postDelayed(this, 500);
            checkTimer();
        }
    };
/*
    // starts google maps with direction to location
    public void startMap() {
        String pass = String.valueOf();             //get value of location from the database
        Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CardListActivity.this, MapDirections.class);
        CardListActivity.this.startActivity(intent);
    }
*/
    //Method used to set off each individual alarm based on the users input
    boolean[] alarmGone = {false, false, false};
    public void checkTimer(){
        int time ;
        if (user != null){
            time = user.getCardTimes().get(0) ;
        }
        else {
            time = 0 ;
        }

        if (localSecond == time && alarmGone[0] == false){ // Alert One
            Intent intent = new Intent(this, StopAlarmActivity.class);
            alarmGone[0] = true ;
            startActivity(intent);



        }

        if (localSecond == time && alarmGone[0] == true && alarmGone[1] == false){ // Alert Two
            Intent intent = new Intent(this, StopAlarmActivity.class);
            alarmGone[1] = true ;
            //startActivity(intent);
            Toast.makeText(getApplicationContext(),
                    "Alarm 2", Toast.LENGTH_LONG).show();
        }
    }

    public void changeColour(){
        FrameLayout layout =(FrameLayout) findViewById(R.id.frame);
         layout.setBackgroundResource(R.drawable.card_state_pressed);
    }
}