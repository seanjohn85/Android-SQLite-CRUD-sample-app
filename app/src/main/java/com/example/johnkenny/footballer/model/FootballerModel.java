package com.example.johnkenny.footballer.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.johnkenny.footballer.Footballer;
import com.example.johnkenny.footballer.database.FootballerContentProvider;
import com.example.johnkenny.footballer.database.FootballerTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by johnkenny on 21/11/2016.
 */

public class FootballerModel {

    //static instance used to create an instance of this class
    private static FootballerModel sFootballerModel;
    private Context mAppContext;
    public static int count;
    //used to create a filter for the content provider
    private String filter;

    /*
    Private contsuctor means it cant be called outside this class to allow control over instances created
    and conform with a sigleton design pattern
     */
    private FootballerModel(Context appContext) {
        mAppContext = appContext;

        //seeds the list of footballers NB COMMENT OUT AFTER FIRST RUN
        seedDatabase();

    }//close constructor


    //used to check if it is already instatiated, either returning the instance of instatating a instance to return
    public static FootballerModel get(Context c) {
        //if the model is null create a new model instance
        if (sFootballerModel == null) {
            sFootballerModel = new FootballerModel(c.getApplicationContext());
        }
        return sFootballerModel;
    }

    //returns the array of footballers from the database
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ArrayList<Footballer> getFootballers() {
        //creates a new arraylist of footballers
        ArrayList<Footballer> footballers = new ArrayList<>();
        /*
         creates a cursor object filled with the sellect all query from the football
          */
        Cursor cursor = mAppContext.getContentResolver().query(FootballerContentProvider.CONTENT_URI, FootballerTable.ALL_COLS,
                null, null, null, null);

        //loops throw the cusror -- all objects from the db
        while (cursor.moveToNext()) {
            //creates  a new footballer object
            Footballer footballer = new Footballer();
            //sets the properties of the footballer object
            footballer.setmId(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_ID)));
            footballer.setName(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_NAME)));
            footballer.setNumber(cursor.getInt(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_NO)));
            footballer.setPostion(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_POSITION)));
            footballer.setPlaying(cursor.getInt(
                    cursor.getColumnIndex(FootballerTable.CURRENTLY_PLAYING)));
            footballer.setBlobber(cursor.getBlob(
                    cursor.getColumnIndex(FootballerTable.IMG)));
            footballer.setDob(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.DOB)));
            footballer.setTeam(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_TEAM)));
            //adds this footballer to the arraylist
            footballers.add(footballer);

        }
        //closes the cursor
        cursor.close();
        //sorts in order of newest added
        Collections.sort(footballers, new Comparator<Footballer>() {
            public int compare(Footballer f1, Footballer f2) {
                return f2.toString().compareTo(f1.toString());
            }
        });
        //returns arraylist of footballers
        return footballers;
    }//getFootballers

    //used to populate the database
    //used for demonstation purposes only
    public void seedDatabase() {
        Footballer footballer = new Footballer();
        //creates a dummy lust of footballers
        for (int i = 0; i < 10; i++) {
            //sets all the footballers values
            footballer.setmId(Integer.toString(i));
            footballer.setName("Wayne Rooney");
            //evrey second one
            int x = i % 2;
            footballer.setNumber(i);
            footballer.setPostion("Striker");
            footballer.setTeam("Everton");
            footballer.setPlaying(x);
            footballer.setDob("27/03/1985");

            try {
                //adds the current footballer object of the loop to the db
                createFootballer(footballer);
            } catch (SQLiteException e) {
                //used to catch and print errors
                e.printStackTrace();
            }
        }//close for loop
    }//close seedDatabase

    //method to create a footballer on the db
    public Footballer createFootballer(Footballer footballer) {
        /*
        creates a new content values object, required to insert values to a db
        the values are preset in the footballer class and retrievd from the footballer object
        passed into this method as a pramater
         */
        ContentValues values = footballer.toValues();
        // mDB.insert(FootballerTable.TABLE_FOOTBALLERS, null, values);
        //creates a uri and sets it to an incert using the values from the footballer object
        Uri foot = mAppContext.getContentResolver().insert(FootballerContentProvider.CONTENT_URI, values);
        // Log.d("created", foot.getLastPathSegment());
        //created Footballer object returned
        return footballer;

    }//close create footballer

    //method used to delete a footballer from the db
    public int deleteFootballer(String footballer_id) {
        //created a string filter required by the content provider used to id the footballer
        filter = FootballerTable.FOOTBALLER_ID + "=" + footballer_id;
        //deletes the footballer and returns one to let the application know the footballer is deleted
        return mAppContext.getContentResolver().delete(FootballerContentProvider.CONTENT_URI, filter, null);

        //return mDB.delete(FootballerTable.TABLE_FOOTBALLERS, FootballerTable.FOOTBALLER_ID + "=" + footballer_id, null) > 0;
    }//close deleteFootballer


    //method uses to update a footballer
    public void updateFootballer(Footballer footballer) {
        //created a string filter required by the content provider used to id the footballer
        filter = FootballerTable.FOOTBALLER_ID + "=" + footballer.getmId();
        //get the ContentValues using the toValues method from the fooballer class
        ContentValues values = footballer.toValues();
        //int update = mDB.update(FootballerTable.TABLE_FOOTBALLERS, values, "id =?", new String[]{footballer.getmId()});\

        //updates the databse using the update method from the content provider
        mAppContext.getContentResolver().update(FootballerContentProvider.CONTENT_URI, values, filter, null);

    }//closes updateFootballer

    //database query to get a single row and uses it to create a footballer object
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Footballer getFootballer(String footballer_id) {
        //created a string filter required by the content provider used to id the footballer
        filter = FootballerTable.FOOTBALLER_ID + "=" + footballer_id;
        //sets a cursor to the recieve a row from the database using an id. uses the filter as the selection id for the content provider
        Cursor cursor = mAppContext.getContentResolver().query(FootballerContentProvider.CONTENT_URI, FootballerTable.ALL_COLS,
                filter, null, null);

        /* Cursor cursor = FootballerContentProvider.queryHack(footballer_id);
        this method was hack that is now removed
         */ // Cursor cursor = mDB.rawQuery("SELECT * FROM myFootballers WHERE id=?", new String[] {footballer_id+""});

        //creates a null footballer object, this is returned if the footballer is not found on the db
        Footballer footballer = null;
        //if the cursor is greater than 0, we have a db match
        if (cursor.getCount() > 0) {
            //moves to the first item of this match
            cursor.moveToFirst();
            //sets the footballer object properties to match the db footballer
            footballer = new Footballer();
            //sets the footballer object with all the properties from the cursor
            footballer.setmId(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_ID)));
            footballer.setName(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_NAME)));
            footballer.setNumber(cursor.getInt(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_NO)));
            footballer.setPostion(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_POSITION)));
            footballer.setPlaying(cursor.getInt(
                    cursor.getColumnIndex(FootballerTable.CURRENTLY_PLAYING)));
            footballer.setBlobber(cursor.getBlob(
                    cursor.getColumnIndex(FootballerTable.IMG)));
            footballer.setDob(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.DOB)));
            footballer.setTeam(cursor.getString(
                    cursor.getColumnIndex(FootballerTable.FOOTBALLER_TEAM)));
        }//ends if statement
        //returns the footballer
        return footballer;
    }//close getFootballer

}//closes the model
