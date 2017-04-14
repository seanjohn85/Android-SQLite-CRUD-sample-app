package com.example.johnkenny.footballer.database;

/**
 * Created by johnkenny on 27/11/2016.
 * This class is used to create a table for the footballer
 */

public class FootballerTable {

    //the database table name
    public static final String DATABASE_NAME = "footballers.db";

    //this is the first version of the applicaption
    //each updated version would require this no to be incremented
    public static final int VERSION = 1;

    //table name
    public static final String TABLE_FOOTBALLERS = "myFootballers";

    //list of columns in the table. matches the footballer object
    public static final String FOOTBALLER_ID = "id";
    public static final String FOOTBALLER_NAME = "name";
    public static final String FOOTBALLER_NO = "number";
    public static final String FOOTBALLER_POSITION = "position";
    public static final String CURRENTLY_PLAYING = "playing";
    public static final String FOOTBALLER_TEAM = "team";
    public static final String IMG = "image";
    public static final String DOB = "dob";


    //all cols array
    public static final String[] ALL_COLS = {FOOTBALLER_ID, FOOTBALLER_NAME, FOOTBALLER_NO,
            FOOTBALLER_POSITION, CURRENTLY_PLAYING, IMG, FOOTBALLER_TEAM, DOB};

    //SQL to create a table
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_FOOTBALLERS +
            " (" + FOOTBALLER_ID + " TEXT PRIMARY KEY , " + FOOTBALLER_NAME + " TEXT, "
            + FOOTBALLER_NO + " INT, " + FOOTBALLER_POSITION + " TEXT, "
            + CURRENTLY_PLAYING + " INT, " + IMG + " BLOB, " + DOB
            + " TEXT, " + FOOTBALLER_TEAM + " TEXT) ";

    //SQL to delete the table
    public static final String SQL_DELETE = "DROP TABLE " + TABLE_FOOTBALLERS;

}
