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

public class AlarmActivity extends AppCompatActivity implements SensorEventListener{

    //Accelerometer Variables
    private SensorManager sensorMan;
    private Sensor accelerometer;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    //Alarm Variables
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    private TimePicker alarmTimePicker;
    private static MainActivity inst;
    private TextView alarmTextView;
    private Context context;

    UserDetails user ;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (UserDetails)extras.getSerializable("Object");
        }
        else{
            user = new UserDetails();
        }

        Intent intent = new Intent(this, AddUserDetailsActivity.class);
        intent.putExtra("Object", user);

        //startTimer();

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        this.context = this;

        // message on open
        Toast.makeText(getApplicationContext(), "Enter your desired alarm time: ", Toast.LENGTH_SHORT).show();

        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // set the alarm to the time that you picked
        final Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.SECOND, 3);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);

        Button start_alarm= (Button) findViewById(R.id.start_alarm);

        start_alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
                final int hour ;
                final int minute ;
                if(user.getAlarmHour() != -1){
                    hour = user.getAlarmHour();
                    minute = user.getAlarmMin();
                }
                else{
                    hour = alarmTimePicker.getHour();
                    minute = alarmTimePicker.getMinute();
                }

                String minute_string = String.valueOf(minute);
                String hour_string = String.valueOf(hour);
                Boolean pm = false;
                String alarmtext;

                if (minute < 10) {minute_string = "0" + String.valueOf(minute);}
                if (hour > 12) {hour_string = String.valueOf(hour - 12); pm = true;}
                if (hour == 12) {pm = true;}

                if(pm){minute_string += " pm";}
                else {minute_string += " am";}

                myIntent.putExtra("extra", "yes");
                myIntent.putExtra("Object", user);

                pending_intent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

                alarmtext = hour_string + ":" + minute_string;

                Toast.makeText(getApplicationContext(), "Alarm set for " + alarmtext , Toast.LENGTH_SHORT).show();

            }

        });

        Button stop_alarm= (Button) findViewById(R.id.stop_alarm);

        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Stop the alarm
                myIntent.putExtra("extra", "no");
                sendBroadcast(myIntent);
                alarmManager.cancel(pending_intent);
                //setAlarmText("Alarm canceled");
                Toast.makeText(getApplicationContext(), "Alarm Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

//    public void openPuzzle(View view){
//        Intent intent = new Intent(this, CardListActivity.class);
//        startActivity(intent);
//        finish();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if(mAccel > 0.5 && timer > 10){
                Toast.makeText(getApplicationContext(), "Movement Detected", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //Timer Variables
    public void startTimer(){

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private int timer  ;

    long startTime = 0;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            timer = seconds ;
            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
