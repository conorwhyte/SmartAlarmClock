package com.example.conorwhyte.smartalarmclock;

/**
 * Inpired by youtube tutorial https://youtu.be/xbBlzOblD10?list=PL4uut9QecF3DLAacEoTctzeqTyvgzqYwA
 * this ringtone service plays the default alarm when called and stops the alarm when called to stop
 */

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


        }
        if(startId ==0)
        {
            ringtone.stop();
        }

        return START_NOT_STICKY;
    }


}