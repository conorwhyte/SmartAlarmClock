package com.example.conorwhyte.smartalarmclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewDetailsActivity extends AppCompatActivity {

    UserDetails user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (UserDetails)extras.getSerializable("Object");
        }
        else{
            user = new UserDetails();
        }


        TextView txt = (TextView) this.findViewById(R.id.textView15);
        String time = Integer.toString(user.returnTotalTime());
        txt.setText("Total Time: " + time);
    }
}
