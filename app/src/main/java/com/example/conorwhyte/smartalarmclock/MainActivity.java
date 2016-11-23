package com.example.conorwhyte.smartalarmclock;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    UserDetails user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        //startTimer();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (UserDetails)extras.getSerializable("Object");
        }
        else{
            user = new UserDetails();
        }

        if(user.firstTime == true){
            //Open PopUp
            user.setFirstTime();
            popUp();

        }


        TextView txt = (TextView) this.findViewById(R.id.textView14);
        String num = Integer.toString(user.numberOfCards());
        txt.setText(num);

/*
        if(user != null){
            String name = Integer.toString(user.getCardTime(0));
            txt.setText(name);
        }
        else{
            txt.setText("ERROR");
        }
        */
    }

    public void popUp() {
        final Intent intent = new Intent(this, AddUserDetailsActivity.class);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("First Time USer");
        alertDialog.setMessage("PLease fill in some details about your morning " +
                ", or being in a location where your GPS location cannot be found");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Intent intent = new Intent(this, AddUserDetailsActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
        alertDialog.show();
    }

    public void openAlarm(View view){
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openManager(View view){
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openDirection(View view){
        Intent intent = new Intent(this, DistanceActivity.class);
        finish();
        startActivity(intent);
    }

    public void openPunisher(View view){
        Intent intent = new Intent(this, PunisherActivity.class);
        startActivity(intent);
    }

    public void openUserDetails(View view){
        Intent intent = new Intent(this, AddUserDetailsActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openSetAlarm(View view){
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
        //finish();
    }

}
