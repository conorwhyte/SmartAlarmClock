package com.example.conorwhyte.smartalarmclock;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

public class CardListActivity extends Activity {

    private static final String TAG = "CardListActivity";
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        listView = (ListView) findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);

        for (int i = 0; i < 10; i++) {
            //Card card = new Card("Card " + (i+1) + " Line 1", "Card " + (i+1) + " Line 2");
            //cardArrayAdapter.add(card);
        }
        final int[] images = {R.drawable.breakfast, R.drawable.shower, R.drawable.suit, R.drawable.food, R.drawable.car};

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

        listView.setAdapter(cardArrayAdapter);

        for (int i = 0 ; i < cardArrayAdapter.getCount(); i++){

            /*cardArrayAdapter.getItem(i);
            FrameLayout layout =(FrameLayout) findViewById(R.id.frame);
            layout.setBackgroundResource(R.drawable.card_state_pressed);
            */

        }

//        cardArrayAdapter.getItem(1).setBackground(this);
  //      listView.setAdapter(cardArrayAdapter);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;

                if (secondsRemaining < 15){

                    //FrameLayout layout =(FrameLayout) findViewById(R.id.frame);
                   // layout.setBackgroundResource(R.drawable.card_state_pressed);
                }

                //cardArrayAdapter.remove(card);
            }

            public void onFinish() {


            }
        }.start();
    }

    public void changeColour(){
        FrameLayout layout =(FrameLayout) findViewById(R.id.frame);
         layout.setBackgroundResource(R.drawable.card_state_pressed);
    }
}