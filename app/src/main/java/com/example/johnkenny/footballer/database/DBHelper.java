package com.example.johnkenny.footballer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by johnkenny on 27/11/2016.
 * this class is used to create the database
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String tag = "DBHelper";

    //constructor with superclass methods from SQLiteOpenHelper
    public DBHelper(Context c)
    {
        super(c, FootballerTable.DATABASE_NAME, null, FootballerTable.VERSION);
    }

    //This is called if the table does not excist already
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FootballerTable.TABLE_CREATE);
        Log.d(tag, FootballerTable.TABLE_CREATE);

    }

    /*
    used for when an updated version to recreate the table by deleting it and recreating it
    note-this deletes all user data
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FootballerTable.TABLE_FOOTBALLERS);
        onCreate(db);

    }
}
