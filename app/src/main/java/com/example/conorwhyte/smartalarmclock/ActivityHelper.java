package com.example.conorwhyte.smartalarmclock;

import android.app.Activity;
import android.content.pm.ActivityInfo;

/**
 * Created by Paul Ledwith on 03/12/2016
 * Class used to set common additions
 * through all activity with an onCreate()
 */

public class ActivityHelper {
    public static void initialize(Activity activity) {
        //Do all sorts of common task for your activities here including:

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}