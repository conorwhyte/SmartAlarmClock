package com.example.conorwhyte.smartalarmclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class StopAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);

        setQuote();


    }


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
}
