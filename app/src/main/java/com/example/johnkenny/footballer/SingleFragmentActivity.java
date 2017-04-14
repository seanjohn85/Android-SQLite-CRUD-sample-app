package com.example.johnkenny.footballer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by johnkenny on 21/11/2016.
 */


//this is an abstract class and the to access a fragment that can be used for every activity that requires the fragment

public abstract class SingleFragmentActivity extends AppCompatActivity {

    //the main FootballerActivity will implement this to create the fragment
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this is the activity this class uses
        setContentView(R.layout.activity_footballer);


        /* the manager is used to handle the list of fragments for this activity
            and adds their views to the list view
          */
        FragmentManager manager = getSupportFragmentManager();

        //this container view informs the manager the fragment should appear in the activity view
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        //verifies the fragment is not equal to null
        if (fragment == null) {
            //assigns the fragment to new fragment
            fragment = createFragment();
            //starts the transaction and commits it
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment).commit();

        }
        fabBtn();


    }//closes onCreate

    //directs to create new
    private void fabBtn() {
        //gets the fab button from the main xml file
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ///adds fab button event listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //gets the application context to start the intent to move a new activity
                Context c = getApplicationContext();
                //opens a new empty FootballerActivity used for create
                Intent intent = new Intent(c, FootballerActivity.class);
                startActivity(intent);

            }
        });

        //sets the image on the fab button
        fab.setImageResource(R.drawable.pl);

    }//clode fabBtn

}//closes this class
