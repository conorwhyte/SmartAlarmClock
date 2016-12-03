package com.example.conorwhyte.smartalarmclock;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


/**
 * Created by Paul Ledwith on 24/11/2016.
 * This is ready to be called destination and current location and transport mode
 * should be taken from main memory and passed into this class
 * when the person should be leaving then house
 */

public class MapDirections extends FragmentActivity implements OnMapReadyCallback {

    UserDetails user;

    private String mode = user.getMode();
    //start locaion
    private double originlat = user.getHome().latitude;
    private double originlon = user.getHome().longitude;
    //destination
    private double deslat = user.getDestination().latitude;
    private double deslon = user.getDestination().longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        ActivityHelper.initialize(MapDirections.this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Toast.makeText(this, deslat + "," + deslon, Toast.LENGTH_SHORT).show();
        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr="
                + originlat + "," + originlon
                + "&daddr=" + "" + deslat + "," + deslon + "&mode=" + mode);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}