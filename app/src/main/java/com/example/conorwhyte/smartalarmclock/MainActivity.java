package com.example.conorwhyte.smartalarmclock;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    static UserDetails user;

    public static SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        boolean newUser = true;             // first time user or no

        mPrefs = getPreferences(MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mPrefs.getString("UserDetails", "");  // try to get UserDetails from memory

        if (!json.isEmpty())         // there is a UserDetails stored in memory
        {
            Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_LONG).show();
            // get user
            user = gson.fromJson(json, UserDetails.class);     // load UserDetails into user object
            user.setFirstTime(false);
            newUser = false;
        } else // empty json string, this is the first time
        {
            Toast.makeText(getApplicationContext(), "First Time User Welcome", Toast.LENGTH_LONG).show();
            user = new UserDetails();
            user.setFirstTime(true);
        }

        //startTimer();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (UserDetails) extras.getSerializable("Object");
        } else if (newUser) {
            user = new UserDetails();
        }

        if (user.getFirstTime() == true && newUser) {
            //Open PopUp
            user.setFirstTime(false);
            popUp();
        }


        TextView txt = (TextView) this.findViewById(R.id.textView14);

        txt.setText(Integer.toString(user.getCardCount()));

        // Add UserDetails object to memory below

        SharedPreferences.Editor prefsEditor = MainActivity.mPrefs.edit();
        gson = new Gson();
        json = gson.toJson(user);
        prefsEditor.putString("UserDetails", json);
        prefsEditor.commit();
    }

    public void popUp() {
        final Intent intent = new Intent(this, AddUserDetailsActivity.class);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("First Time User");
        alertDialog.setMessage("Please fill in some details about your morning routine!\n" +
                "Please turn location services on," +
                " or being in a location where your GPS location cannot be found" +
                " you can enter your location manually.");
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

    public void openAlarm(View view) {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openManager(View view) {
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openDirection(View view) {
        Intent intent = new Intent(this, DistanceActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openUserDetails(View view) {
        Intent intent = new Intent(this, AddUserDetailsActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openSetAlarm(View view) {
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
        //finish();
    }
}
