package com.example.conorwhyte.smartalarmclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class AddUserDetailsActivity extends AppCompatActivity {

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<Integer> time = new ArrayList<Integer>();
    int count = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_details);
    }

    public void addDetails(View view){

        EditText editText = (EditText) findViewById(R.id.editText4);
        String morningName = editText.getText().toString();

        EditText editText1 = (EditText) findViewById(R.id.editText3);
        //String timeNeeded = editText.getText().toString();

        int timeRequired = Integer.parseInt(editText1.getText().toString());


        count++ ;
        name.add(morningName);
        time.add(timeRequired);

        /*Intent i = new Intent(getApplicationContext(), AddUserDetailsActivity.class);
        finish();
        startActivity(i);
        */
    }

    UserDetails newUser = new UserDetails();
    public void sendDetails(View view){


        newUser.setCardWakeTime(time);
        newUser.setCardName(name);
        newUser.setNumber(count);
        newUser.setFirstTime();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("Object", newUser);
        startActivity(i);
        finish();
    }
}
