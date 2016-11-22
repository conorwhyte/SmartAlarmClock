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
   //Intent i;


    @Override
    public void onReceive(Context context, Intent intent) {
       String state = intent.getExtras().getString("extra");
        Toast.makeText(context, "Recieved!!", Toast.LENGTH_LONG).show();
        Intent i = new Intent();
        i.setClassName("com.example.conorwhyte.smartalarmclock", "com.example.conorwhyte.smartalarmclock.CardListActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        /**  Intent service_intent = new Intent(context, RingtonePlayingService.class);
        context.startActivity(i);service_intent.putExtra("extra", state);
        service_intent.putExtra("quote id", richard_id);
        context.startService(service_intent);    **/






        //Log.e("MyActivity", "In the receiver with " + state);




    }
}
