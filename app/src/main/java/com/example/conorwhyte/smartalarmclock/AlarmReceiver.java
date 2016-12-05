package com.example.conorwhyte.smartalarmclock;
/**
 * Created by Padraig Mitchell this broadcast intent waits for the Alarm manager to send the pending
 * intent and then decides basend on the the exttra's what to do if the alarm is being set off a
 * service is started to play an alarm to notify the user and if the alarm is stopped the user is
 * brought to morning routine page. Inspired by
 * youtube tutorial https://youtu.be/xbBlzOblD10?list=PL4uut9QecF3DLAacEoTctzeqTyvgzqYwA
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by padraigmitchell on 08/11/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        UserDetails user;
        String state = extras.getString("extra", "yes");

        if (extras != null) {
            user = (UserDetails) extras.getSerializable("Object");
        } else {
            user = new UserDetails();
        }


        if (state == "yes") {
            Toast.makeText(context, "Beep beep, time to move!", Toast.LENGTH_LONG).show();

        } else {
            Intent i = new Intent();
            i.putExtra("Object", user);
            i.setClassName("com.example.conorwhyte.smartalarmclock", "com.example.conorwhyte.smartalarmclock.CardListActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        service_intent.putExtra("extra", state);
        context.startService(service_intent);


    }}