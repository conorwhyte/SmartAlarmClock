package com.example.conorwhyte.smartalarmclock;

/**
 * Created by Paul Ledwith on 15/11/2016.
 */


import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.v7.media.MediaControlIntent.EXTRA_MESSAGE;


public class DistanceActivity extends FragmentActivity implements android.location.LocationListener {

    ArrayList<LatLng> markerPoints;
    TextView tvDistanceDuration;
    private RadioGroup radioGroup;
    private String mode = "driving";
    public double latitude = 0.0;
    public double longitude = 0.0;
    public double deslat = 0.0;
    public double deslon = 0.0;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        // check for location permission
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Context.LOCATION_SERVICE);

        if (permissionCheck == -1)      // if permission is not granted
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);     // ask for permission !
        }

        tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);
        markerPoints = new ArrayList<LatLng>();

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
                LatLng location = getLocationFromAddress(locationPostalAddress.getText().toString());
                LatLng destination = getLocationFromAddress(destinationPostalAddress.getText().toString());
                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(location, destination, mode);
                // Start downloading json data from Google Directions API
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }
        });

    }

    public void sendMessage(View view) {
        String pass = String.valueOf(latitude);
        Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DistanceActivity.this, MapDirections.class);
        DistanceActivity.this.startActivity(intent);
    }

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


    private String getDirectionsUrl(LatLng loca, LatLng dest, String mode) {
        // Origin of route
        String str_origin = "";
        getLocation();
        if(loca == null) {
            str_origin = "origin=" + latitude + "," + longitude;
        }
        else {
            str_origin = "origin=" + loca.latitude + "," + loca.longitude;
            latitude = loca.latitude;
            longitude = loca.longitude;
        }
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        deslat = dest.latitude;
        deslon = dest.longitude;

        // Travel mode
        String str_mode = "mode=" + mode;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + str_mode + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception download url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

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
                //Toast.makeText(DistanceActivity.this, "Location Services Must be turned on", Toast.LENGTH_SHORT).show();
                return;
            }

            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                Toast.makeText(DistanceActivity.this, "GPS is on!", Toast.LENGTH_SHORT).show();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
    }

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
                    Toast.makeText(this, "Location Services is enabled", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Location Services is disabled", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);

        //open the map:
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

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        JSONObject jObject;
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {


            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            String distance = "";
            String duration = "";


            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }


            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                }

            }
            tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);

        }
    }


}
