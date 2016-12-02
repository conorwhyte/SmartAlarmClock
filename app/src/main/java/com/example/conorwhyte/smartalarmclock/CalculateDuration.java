package com.example.conorwhyte.smartalarmclock;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Paul Ledwith on 02/12/2016
 */

class CalculateDuration {

    UserDetails user;

    // method for putting the url together
    public void getDirectionsUrl() {
        String str_mode;
        // Origin of route
        String str_origin = "origin=" + user.getHomeLat() + "," + user.getHomeLon();

        // Destination of route
        String str_dest = "destination=" + user.getDestLat() + "," + user.getDestLon();

        // Travel mode
        String mode = "driving";
        if (mode.equals("transit")) {
            str_mode = "mode=" + mode + "&arrival_time=" + returnSeconds();
        } else {
            str_mode = "mode=" + mode;
        }

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + str_mode + "&" + sensor;

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }

    // A method to download json data from url
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        HttpURLConnection urlConnection;
        URL url = new URL(strUrl);

        // Creating an http connection to communicate with url
        urlConnection = (HttpURLConnection) url.openConnection();

        // Connecting to url
        urlConnection.connect();

        // Reading data from url
        try (InputStream iStream = urlConnection.getInputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception download url", e.toString());
        } finally {
            urlConnection.disconnect();
        }
        return data;
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
            String duration = "";
            int hours;
            int minutes;
            Pattern time = Pattern.compile("(\\d+)(?:\\D+)(\\d+)(?:\\D+)");
            Matcher m;


            if (result.size() < 1) {
                return;
            }


            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                }
            }
            if (duration.length() < 8) {
                time = Pattern.compile("(\\d+)(?:\\D+)");
                m = time.matcher(duration);
                if (m.matches()) {
                    minutes = Integer.parseInt(m.group(1));
                    System.out.println("minutes: " + minutes);
                    user.setJourneyTime(minutes);
                }
            } else {
                m = time.matcher(duration);
                if (m.matches()) {
                    hours = Integer.parseInt(m.group(1));
                    minutes = Integer.parseInt(m.group(2));
                    System.out.println("hour: " + hours);
                    hours = hours * 60;
                    user.setJourneyTime(minutes + hours);
                }
            }
        }
    }

    private long returnSeconds() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        calendar1.set(1970, 1, 1, 0, 0);
        if (Calendar.HOUR_OF_DAY < user.getArrivalHour() || (Calendar.HOUR_OF_DAY == user.getArrivalHour() && Calendar.MINUTE <= user.getArrivalMin())) {
            calendar2.set(year, month, day + 1, user.getArrivalHour(), user.getArrivalMin());
        } else {
            calendar2.set(year, month, day, user.getArrivalHour(), user.getArrivalMin());
        }
        long milliseconds1 = calendar1.getTimeInMillis();
        long milliseconds2 = calendar2.getTimeInMillis();
        long diff = milliseconds2 - milliseconds1;
        return diff / 1000;
    }
}
