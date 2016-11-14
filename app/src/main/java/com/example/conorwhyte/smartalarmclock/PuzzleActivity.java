package com.example.conorwhyte.smartalarmclock;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class PuzzleActivity extends AppCompatActivity {

    MediaPlayer media_song ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        /*
        media_song = MediaPlayer.create(this, R.raw.killerwhale_resident);
        media_song.start();
        */ 
    }

    String puzzleString ;
    int result ;
    public void puzzleGame(){
        Random rand = new Random();
        int  random = rand.nextInt(2) + 1;
        //int random = 0 ;


        switch(random){
            case 1: puzzleString = "2 * (2 + 2)";
                result = 8 ;
                break;
            case 2: puzzleString = "6 + 6 - 3" ;
                result = 9  ;
            default:
                break ;
        }
        TextView problemField = (TextView)this.findViewById(R.id.textView) ;
        problemField.setText(String.valueOf(puzzleString));
    }

    public void onClick(View view){
        Intent intent = new Intent(this, PunisherActivity.class);
        startActivity(intent);
    }

    public void checkResult(View view){
        EditText mEdit   = (EditText)findViewById(R.id.editText);
        TextView txt = (TextView)findViewById(R.id.textView2) ;

        String answer ;
        answer =  mEdit.getText().toString();

        int intAnswer = Integer.parseInt(answer);
        if ( intAnswer == result){
            txt.setText("true");
            //Turn off the Alarm
            media_song.stop();
        }
        else{
            txt.setText("false");
            //Keep Blasting that shit
        }
    }
}
