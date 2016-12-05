package com.example.conorwhyte.smartalarmclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * This activity is used not only to set off the alarm noise, but also to stop and snooze it. It sets
 * a daily motivational quote at random also. This is simply the traditional stop and start alarm.
 * <p>
 * Author: Conor Whyte
 */

public class StopAlarmActivity extends AppCompatActivity implements SensorEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);
        ActivityHelper.initialize(StopAlarmActivity.this);
        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        if (isMovementEnough()) {
            stopAlarm();
        }

        setQuote();
    }

    //Sets the daily quote at random
    String quote;
    String author;

    public void setQuote() {
        Random rand = new Random();
        int random = rand.nextInt(3) + 1;

        switch (random) {
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

        TextView quoteField = (TextView) this.findViewById(R.id.textView);
        quoteField.setText(String.valueOf(quote));

        TextView authorField = (TextView) this.findViewById(R.id.textView5);
        authorField.setText(String.valueOf(author));

    }

    public void stopAlarm(){
        Intent service_intent = new Intent(getApplicationContext(), RingtonePlayingService.class);
        service_intent.putExtra("extra", "no");
        getApplicationContext().startService(service_intent);
        finish();
    }

    //Used to stop the alarm when button is pressed
    public void stopButton(View view) {
        stopAlarm();
        openCardList();
    }

    public void openCardList() {
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra("Object", MainActivity.user);
        startActivity(intent);
        //finish();
    }

    public void pushNotification(){
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        PendingIntent.getActivity(StopAlarmActivity.this, 0, notificationIntent, 0);
        Intent intent = new Intent(this, StopAlarmActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setCategory(Notification.CATEGORY_PROMO)
                .setContentTitle("Morning Manager")
                .setContentText("Its Time to Start")
                .setSmallIcon(R.drawable.car)
                .setAutoCancel(true)
                .setVisibility(1)
                .addAction(android.R.drawable.ic_lock_idle_alarm, "Edit Activity", pi)
                .setContentIntent(pi)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }


    //Used to snooze the alarm once the button is pressed
    public void snoozeButton(View view) {

        new CountDownTimer(30000, 1000) {       //set snooze time here
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;

                if (secondsRemaining < 15) {

                    //FrameLayout layout =(FrameLayout) findViewById(R.id.frame);
                    // layout.setBackgroundResource(R.drawable.card_state_pressed);
                }

                //cardArrayAdapter.remove(card);
            }

            public void onFinish() {

            }
        }.start();
    }


    private SensorManager sensorMan;
    private Sensor accelerometer;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            detectSignificantMovement(mAccel);
            //this should be changed with a more accurate set of data in the future
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    ArrayList<Float> arrayAccel = new ArrayList<Float>();
    public void detectSignificantMovement(float currentAcceleration) {
        double minShake = 3.0;

        if (currentAcceleration > minShake) {
            boolean isShaking = true;
        } else if (currentAcceleration <= minShake) {
            //isShaking = false ;
        }
        arrayAccel.add(currentAcceleration);
    }

    public boolean isMovementEnough() {
        boolean check;
        int count = 0;

        for (Float f : arrayAccel) {
            if (f > 3.0) {   //min shake required for movement
                count++;
            }
        }
        //num of shakes of a minRequirement needed for "walking" to be detected
        check = count > 30;
        return check;
    }

}
