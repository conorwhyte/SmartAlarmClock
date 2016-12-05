package com.example.conorwhyte.smartalarmclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.conorwhyte.smartalarmclock.R;

public class RingtonePlayingService extends Service {
    MediaPlayer media_song;
    private boolean isRunning;
    private Context context;
    private int startId;
    private Ringtone ringtone;


    @Override
    public IBinder onBind(Intent intent)
    {
        Log.e("MyActivity", "In the Richard service");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        String state = intent.getExtras().getString("extra");
        assert state != null;
        switch (state)
        {
            case "no":
                startId = 0;
                break;
            case "yes":
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }
        if (!this.isRunning && startId == 1)
        {
            Log.e("Going on here  ", "Served");
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(this, uri);
            ringtone.play();

            //Intent intent2 = new Intent(this, PunisherActivity.class);
            //startActivity(intent2);
            //finish();

        }

        if(startId ==0)
        {
            ringtone.stop();
        }

        return START_NOT_STICKY;
    }


}