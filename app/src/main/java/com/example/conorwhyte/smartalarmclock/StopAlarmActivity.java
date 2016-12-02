package com.example.conorwhyte.smartalarmclock;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * This activity is used not only to set off the alarm noise, but also to stop and snooze it. It sets
 * a daily motivational quote at random also. This is simply the traditional stop and start alarm.
 *
 * Author: Conor Whyte
 */

public class StopAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);
        setQuote();
        Intent service_intent = new Intent(getApplicationContext(), RingtonePlayingService.class);
        service_intent.putExtra("extra", "yes");
        getApplicationContext().startService(service_intent);

    }

    //Sets the daily quote at random
    String quote ;
    String author ;
    public void setQuote(){
        Random rand = new Random();
        int  random = rand.nextInt(3) + 1;

        switch(random){
            case 0:
                quote = "'Lose an hour in the morning, and you will be all day hunting for it.'";
                author = "-Richard Whately";
                break;

            case 1:
                quote = "'Life is too short,” she panicked, “I want more.” He nodded slowly, Wake up earlier'";
                author = "- Dr. SunWolf";
                break;

            case 2:
                quote = "'It is well to be up before daybreak, for such habits contribute to health, wealth, and wisdom.'";
                author = "-Aristotle ";
                break;

            case 3:
                quote = "'Early to bed and early to rise, makes a man healthy, wealthy and wise.'";
                author = "-Benjamin Franklin ";
                break;

            default:
                quote = "'I never knew a man come to greatness or eminence who lay abed late in the morning.'";
                author = "- Johnathan Swift";
                break;
        }

        TextView quoteField = (TextView)this.findViewById(R.id.textView) ;
        quoteField.setText(String.valueOf(quote));

        TextView authorField = (TextView)this.findViewById(R.id.textView5) ;
        authorField.setText(String.valueOf(author));

    }

    //Used to stop the alarm when button is pressed
    public void stopButton(View view){
        Intent service_intent = new Intent(getApplicationContext(), RingtonePlayingService.class);
        service_intent.putExtra("extra", "no");
        getApplicationContext().startService(service_intent);
        finish();
    }

    //Used to snooze the alarm once the button is pressed
    public void snoozeButton(View view){

        new CountDownTimer(30000, 1000) {       //set snooze time here
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
}
