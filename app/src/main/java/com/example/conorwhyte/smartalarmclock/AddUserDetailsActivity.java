package com.example.conorwhyte.smartalarmclock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The following class is used to get the user Details for the cards, or the morning routine.
 * Each one has a name and a time associated with it, which is an indication as to how long the
 * activity will take. If null a popup will show up to prompt the user to try again. This will
 * be opened on the first time the user uses the app. It then sends the user object back to the
 * main activity with all relevant information stored in the user object.
 *
 * Author: Conor Whyte
 */

public class AddUserDetailsActivity extends AppCompatActivity {

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<Integer> time = new ArrayList<Integer>();
    int count = 0 ;
    int totalTime = 0;

    UserDetails newUser = new UserDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_details);
    }

    //add a morning routine
    public void addDetails(View view){

        EditText editText = (EditText) findViewById(R.id.editText4);
        String morningName = editText.getText().toString();

        EditText editText1 = (EditText) findViewById(R.id.editText3);
        String timeNeeded = editText.getText().toString();

        if(editText1.getText().toString().trim().length() == 0 || editText.getText().toString().trim().length() == 0){
            popUpEmptyTextView();
        }
        else {
            int timeRequired = Integer.parseInt(editText1.getText().toString());
            count++;
            int progress = count;
            updateProgress(progress);       // updates activity count
            updateTime(timeRequired);      // update total time
            name.add(morningName);
            time.add(timeRequired);
        }

        /*Intent i = new Intent(getApplicationContext(), AddUserDetailsActivity.class);
        finish();
        startActivity(i);
        */
    }

    //finish with the morning routines and go back to menu, sends user object back
    public void sendDetails(View view)
    {
        for(int i = 0; i < time.size(); i++)
        {
            newUser.addCard(name.get(i));
            newUser.addTime(time.get(i));
        }

        newUser.setFirstTime(false);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("Object", newUser);
        startActivity(i);
        finish();
    }

    //popup for if the user has not entered details correctly
    public void popUpEmptyTextView() {
        final Intent intent = new Intent(this, AddUserDetailsActivity.class);
        AlertDialog alertDialog = new AlertDialog.Builder(AddUserDetailsActivity.this).create();
        alertDialog.setTitle("ERROR");
        alertDialog.setMessage("One of the fields have been left empty, please try again."
        );
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

    public void updateProgress(Integer count)
    {

        TextView textView = (TextView) findViewById(R.id.activityCount);
        textView.setText(count + " Activities");

        return;
    }

    public void updateTime(Integer time)
    {
        totalTime += time;
        TextView textView = (TextView) findViewById(R.id.totalTime);
        textView.setText(totalTime + " Minutes Total");

        return;
    }
}