package com.example.johnkenny.footballer;

import android.content.ContentValues;

import com.example.johnkenny.footballer.database.FootballerTable;

/**
 * Created by john kenny on 09/11/2016..
 * This class is used to create objects
 */

public class Footballer {

    //instance varibles of this object
    private String mId;
    private String team;
    //private String imageLoc;
    private String name;
    private int number;
    private int playing;
    private String postion;
    private byte[] blobber;
    private String dob;


    //Constructors to create objects of this class
    public Footballer(String name) {
        this.name = name;
    }

    public Footballer() {

    }

    //get methods

    /*public String getImageLoc() {
        return imageLoc;
    }*/

    public int getNumber() {
        return number;
    }

    public int getPlaying() {
        return playing;
    }

    public String getName() {
        return name;
    }

    public String getPostion() {
        return postion;
    }

    public String getTeam() {
        return team;
    }

    public String getmId() {
        return mId;
    }

    public byte[] getBlobber() {
        return blobber;
    }

    public String getDob() {
        return dob;
    }

    //set methods

    public void setBlobber(byte[] blobber) {
        this.blobber = blobber;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPlaying(int playing) {
        this.playing = playing;
    }

   /* public void setImageLoc(String imageLoc) {
        this.imageLoc = imageLoc;
    }*/

    public void setPostion(String postion) {
        this.postion = postion;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    //print method used for testing only
    @Override
    public String toString() {
        return name + " " + number + " " + postion + " " + team;
    }

    /* this used to set the values of a footballer object and is used to pust the object to the database*/
    public ContentValues toValues() {

        /*
        crestes a new ContentValues object used to insert/ update db
         */
        ContentValues values = new ContentValues(8);

        //sets the values using all this objects vars
        values.put(FootballerTable.FOOTBALLER_ID, mId);

        values.put(FootballerTable.FOOTBALLER_NAME, name);

        values.put(FootballerTable.FOOTBALLER_NO, number);

        values.put(FootballerTable.CURRENTLY_PLAYING, playing);

        values.put(FootballerTable.FOOTBALLER_POSITION, postion);

        //values.put(FootballerTable.IMG, imageLoc);

        values.put(FootballerTable.IMG, blobber);

        values.put(FootballerTable.DOB, dob);

        values.put(FootballerTable.FOOTBALLER_TEAM, team);

        return values;

    }//ContentValues

}//close Footballer class
