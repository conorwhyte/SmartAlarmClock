package com.example.conorwhyte.smartalarmclock;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //startTimer();

    }

    public void openAlarm(View view){
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    public void openManager(View view){
        Intent intent = new Intent(this, CardListActivity.class);
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
}
