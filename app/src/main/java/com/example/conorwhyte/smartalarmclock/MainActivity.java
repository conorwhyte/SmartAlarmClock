package com.example.conorwhyte.smartalarmclock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    static UserDetails user;

    public static SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Morning Manager");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

//        txt.setText(Integer.toString(user.getCardCount()));

        // Add UserDetails object to memory below

        ImageButton ib3 = (ImageButton) findViewById(R.id.ib3);
        //ImageButton ib4 = (ImageButton) findViewById(R.id.ib4);

        // Set the third image button background transparent
        // This method allow us to show click effect
        TypedValue outValue = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(
                android.R.attr.selectableItemBackground, outValue, true);
        ib3.setBackgroundResource(outValue.resourceId);

        /*
        TypedValue outValue1 = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(
                android.R.attr.selectableItemBackground, outValue1, true);
        ib4.setBackgroundResource(outValue.resourceId);
        */

        SharedPreferences.Editor prefsEditor = MainActivity.mPrefs.edit();
        gson = new Gson();
        json = gson.toJson(user);
        prefsEditor.putString("UserDetails", json);
        prefsEditor.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            openCardList();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            openAddUserDetails();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            openAlarm();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void openAlarm() {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openManager() {
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }


    public void openDirection(View view){
        Intent intent = new Intent(this, NavActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openAddUserDetails() {
        Intent intent = new Intent(this, AddUserDetailsActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
    }

    public void openCardList() {
        Intent intent = new Intent(this, CardListActivity.class);
        intent.putExtra("Object", user);
        startActivity(intent);
        //finish();
    }
}
