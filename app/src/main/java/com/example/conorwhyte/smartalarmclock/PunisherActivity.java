package com.example.conorwhyte.smartalarmclock;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class PunisherActivity extends AppCompatActivity implements LocationListener, SensorEventListener {

    MediaPlayer media_song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punisher);

        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        checkSMSPermissions();
        checkPermissions();

        //media_song = MediaPlayer.create(this, R.raw.killerwhale_resident);
        //media_song.start();

        startLocationTracker();
        final TextView mTextField = (TextView) findViewById(R.id.textView3);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                mTextField.setText("Seconds Remaining: " + secondsRemaining);

                if (secondsRemaining < 13) {
                    vibratePhone();
                    //play ringtone again
                    media_song.start();
                }

                if (isMovementEnough() == true) {
                    mTextField.setText("You're Up");
                    media_song.stop();
                }
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                if (isMovementEnough() == false) {
                    mTextField.setText("Your Fucked");
                    sendMessage();
                    media_song.stop();
                    arrayAccel.clear();
                } else {
                    mTextField.setText("Done");
                    arrayAccel.clear();
                    media_song.stop();
                    finish();
                }
            }
        }.start();

    }


    public void vibratePhone() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200);
    }

    //Send the Dreaded Text
    public boolean checkSMSPermissions() {
        boolean check = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION);

            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                check = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, ACCESS_LOCATION);
                checkSMSPermissions();
            }
        }
        return check;
    }
    public final int ACCESS_SMS = 10;
    public void sendMessage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Send SMS", "");

                String phoneNo = "0833076886";
                String message = "Tim, you suck";

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else {

                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, ACCESS_SMS);   //shouldnt be access location
                sendMessage();
            }
        }
    }


    public boolean checkPermissions() {
        boolean check = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION);

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                check = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION);
                checkPermissions();
            }
        }
        return check;
    }
    private static final int ACCESS_LOCATION = 10;

    public void startLocationTracker() {

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION);

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION);
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            this.onLocationChanged(null);
        }
    }

    private float speed;
    public void onLocationChanged(Location location) {
        TextView txt = (TextView) this.findViewById(R.id.textView4);

        if (location == null) {
            txt.setText(" - m/s");
        } else {
            speed = location.getSpeed();
            float nCurrentSpeed = location.getSpeed();
            txt.setText(nCurrentSpeed + " m/s");

            if (nCurrentSpeed == 0.0) {
                //noticeStatusChange(location);
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private SensorManager sensorMan;
    private Sensor accelerometer;
    private float[] mGravity;
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

    private boolean isShaking = false; //detect is accelerometer is in use

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
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

    private boolean isMoving = false;

    public void checkMovement() {
        if (isShaking == true || speed > 0) {
            isMoving = true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    ArrayList<Float> arrayAccel = new ArrayList<Float>();
    public void detectSignificantMovement(float currentAcceleration) {
        double minShake = 3.0;

        if (currentAcceleration > minShake) {
            isShaking = true;
        } else if (currentAcceleration <= minShake) {
            //isShaking = false ;
        }
        arrayAccel.add(currentAcceleration);
    }

    public boolean isMovementEnough() {
        boolean check = false;
        int count = 0;

        for (Float f : arrayAccel) {
            if (f > 3.0) {   //min shake required for movement
                count++;
            }
        }
        if (count > 30) {    //num of shakes of a minRequirement needed for "walking" to be detected
            check = true;
        } else {
            check = false;
        }
        return check;
    }

}