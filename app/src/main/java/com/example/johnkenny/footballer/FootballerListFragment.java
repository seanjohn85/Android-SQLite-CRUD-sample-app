package com.example.johnkenny.footballer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.johnkenny.footballer.model.FootballerModel;

import java.util.ArrayList;

import static com.example.johnkenny.footballer.R.id.imageView;
import static com.example.johnkenny.footballer.helperClass.imageConvertor.getImage;

/**
 * Created by johnkenny on 26/11/2016.
 */

public class FootballerListFragment extends ListFragment {

    //class level vars
    private static final String TAG = "FootballerListFragment";
    private ArrayList<Footballer> mfootballers;

    //onCreate method
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //gets all the footballers in an arraylist from the db using the model
        mfootballers = FootballerModel.get(getActivity()).getFootballers();

        //creates an array adapter with the footballer arraylist
        FootballAdapter footballAdapter = new FootballAdapter(mfootballers);
        //sets this this adapter with the footballers
        setListAdapter(footballAdapter);

    }//close onCreate method

    //when an footballer on the list is clicked
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //gets this clicked footballer object
        Footballer footballer = (Footballer) (getListAdapter()).getItem(position);
        //used for testing
        Log.d(TAG, footballer.getName() + " was clicked");

        //sends the clicked footballer to footballer activitiy
        //the clicked footballer will be displayed in the footballer fragment
        Intent intent = new Intent(getActivity(), FootballerActivity.class);
        intent.putExtra(FootballerFragment.FB_ID, footballer.getmId());
        startActivity(intent);

    }//onListItemClick

    //a new class used to populate the list with the footballer
    //using each footballer object and the layout items from the xml file
    private class FootballAdapter extends ArrayAdapter<Footballer> {

        // Constructor
        public FootballAdapter(ArrayList<Footballer> f) {
            super(getActivity(), 0, f);

        }


        //   @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //checks for a list viev
            if (null == convertView) {
                //if there is no list view one is inflated here
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_footballer, null);
            }

            // configure the view for this footballer
            Footballer f = getItem(position);

            //set the footballer name in the main text view using the footballer object
            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.name);
            titleTextView.setText(f.getName());

            //Sets the footballer number displayed on the right of each item
            TextView noTextView =
                    (TextView) convertView.findViewById(R.id.f_num);
            noTextView.setText(String.valueOf(f.getNumber()));

            //sets the footballers current team
            TextView ctTextView =
                    (TextView) convertView.findViewById(R.id.currentteam);
            ctTextView.setText(f.getTeam());

            //sets teh footballers position
            TextView posTextView =
                    (TextView) convertView.findViewById(R.id.pos);
            posTextView.setText(f.getPostion());

            /* set the image to the left of the listview item*/
            ImageView iv = (ImageView) convertView.findViewById(imageView);

            //checks if this footballer object has an image set
            if (f.getBlobber() != null) {
                /*if the footballer has an image use the helper class to convert the
                byte array to a bitmap and display this bitmap in the imageview
                 */
                iv.setImageBitmap(getImage(f.getBlobber()));
            } else {
                //if the object has no image use the default image from the drawable folder
                iv.setImageResource(R.drawable.pl);
            }

            //sets the colours of the list items. Every second item a differnt color
            if (position % 2 == 1) {
                convertView.setBackgroundColor(Color.rgb(0, 242, 253));
            } else {
                convertView.setBackgroundColor(Color.rgb(0, 255, 149));
            }
            return convertView;
        }
    }// End of the inner class


    /* this method is for when we return to this screen from another rscreen*/
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onResume() {
        super.onResume();
        //gets the updated list
        updateUI();
    }


    /* this method is used to update the ui with the footballers from the db
    this is needed every time we return to this screen
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void updateUI() {

        //creates a model
        FootballerModel footballerModel = FootballerModel.get(getActivity());
        //populates an arraylist of footballers from the model
        ArrayList<Footballer> mfootballers = footballerModel.getFootballers();
        FootballAdapter footballAdapter = new FootballAdapter(mfootballers);
        setListAdapter(footballAdapter);

    }//updateUI

}

