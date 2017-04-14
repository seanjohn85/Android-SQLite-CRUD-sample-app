package com.example.johnkenny.footballer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

public class FootballerActivity extends SingleFragmentActivity {


    //new fragment is created using the FootballerFragment class
    @Override
    protected Fragment createFragment() {

        return new FootballerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //uses the super class
        super.onCreate(savedInstanceState);
        //loads the new fab button settings
        fabBtn();

    }

    //directs to the list view
    private void fabBtn() {
        //gets the fab button from the main xml file
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ///adds fab button event listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //gets the application context to start the intent to move a new activity
                Context c = getApplicationContext();
                //opens a new empty FootballerlisstActivity used for create
                Intent intent = new Intent(c, FootballerListActivity.class);
                startActivity(intent);

            }
        });

    }//close fabBtn


}
