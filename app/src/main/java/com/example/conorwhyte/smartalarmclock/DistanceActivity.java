package com.example.conorwhyte.smartalarmclock;

/**
 * Created by Paul Ledwith on 15/11/2016.
 * <p>
 * This class gets the users prefrences for a start location,
 * an end location, and the travel mode and makes a request
 * to google for a JSON file, from which we send to the
 * DirectionJSONParser Class. This returns the distance and
 * the duration. These are then printed to the screen and
 * passed to other methods.
 */


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/*
                RETURNSECONDS NEEDS AN EDIT
            NEEDS INTERNAL STORAGE OF ALARM TIME
*/
public class DistanceActivity extends Activity implements android.location.LocationListener {

    ArrayList<LatLng> markerPoints;
    private RadioGroup radioGroup;
    public String mode = "driving";
    public double latitude = 0.0;
    public double longitude = 0.0;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    public LatLng destination;
    public LatLng location;
    UserDetails user;

    //waits for a button press to start actvity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        ActivityHelper.initialize(DistanceActivity.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (UserDetails) extras.getSerializable("Object");
        } else {
            user = new UserDetails();
        }

        // check for location permission
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Context.LOCATION_SERVICE);

        if (permissionCheck == -1)      // if permission is not granted
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);     // ask for permission !
        }

        markerPoints = new ArrayList<>();


        radioGroup = (RadioGroup) findViewById(R.id.radioButton);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos = radioGroup.indexOfChild(findViewById(checkedId));
                switch (pos) {
                    case 0:
                        mode = "driving";
                        break;
                    case 1:
                        mode = "bicycling";
                        break;
                    case 2:
                        mode = "walking";
                        break;
                    case 3:
                        mode = "transit";
                        break;
                }
            }
        });

        Button search = (Button) findViewById(R.id.button2);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText locationPostalAddress = (EditText) findViewById(R.id.autocomplete);
                EditText destinationPostalAddress = (EditText) findViewById(R.id.autocomplete2);
                location = getLocationFromAddress(locationPostalAddress.getText().toString());
                destination = getLocationFromAddress(destinationPostalAddress.getText().toString());
                if (location != null) {
                    user.setHomeLon(location.longitude);
                    user.setHomeLat(location.latitude);
                    user.setHome(location);
                } else {
                    user.setHomeLon(longitude);
                    user.setHomeLat(latitude);
                    user.setHome(new LatLng(latitude, longitude));
                }
                user.setDestLon(destination.longitude);
                user.setDestLat(destination.latitude);
                user.setDestination(destination);
                user.setMode(mode);
            }
        });


    }

    // Gets the latitude and longitude from a string address
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
    }
    // gets the seconds from epoch time


    public static boolean isLocationEnabled(Context context) {
        return true;
    }

    // gets current location
    protected void getLocation() {
        if (isLocationEnabled(DistanceActivity.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

//            int permissionCheck = ContextCompat.checkSelfPermission(this,
//                    Context.LOCATION_SERVICE);
//
//            if(permissionCheck == -1)       // permission denied
//            {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                        1);
//            }

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        } else {
            //prompt user to enable location....
            //.................
        }
    }

    //asks for location services permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Permission granted to your Location Services", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to your Location Services", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // updates the users location
    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}