package com.example.conorwhyte.smartalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by padraigmitchell on 08/11/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("WE are in the reciever.", "teat");
        String state = intent.getExtras().getString("extra");

        String richard_id = intent.getExtras().getString("quote id");

        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        service_intent.putExtra("extra", state);
        service_intent.putExtra("quote id", richard_id);
        context.startService(service_intent);


        Log.e("MyActivity", "In the receiver with " + state);




    }
}
