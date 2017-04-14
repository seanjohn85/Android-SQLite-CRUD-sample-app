package com.example.johnkenny.footballer.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by johnkenny on 05/12/2016.
 */

public class FootballerContentProvider extends ContentProvider {

    //database related varibles
    private static SQLiteDatabase mDB;
    private SQLiteOpenHelper mDbHelper;

    /*
    Each file can only have one AUTHORITY this is also set in manifest using the provider tag
     */
    private static final String AUTHORITY = "com.example.johnkenny.footballer.database.footballercontentprovider";
    private static final String BASE_PATH = "footballer";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    // used for the UriMacher
    private static final int FOOTBALLER = 1;
    private static final int FOOTBALLER_ID = 2;


    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        //used for query all
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, FOOTBALLER);
        //used for query with id
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FOOTBALLER_ID);
    }


    @Override
    public boolean onCreate() {
        //creates an object of the DBHelper class in database folder
        mDbHelper = new DBHelper(getContext());

        //returns the footballer database object
        mDB = mDbHelper.getWritableDatabase();
        return true;
    }

    /*
    used to get rows from the db
     */

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //this is used to check if the query is to find by id and the id is
        //set if true otherwise all data will be reterived
        if (sURIMatcher.match(uri) == FOOTBALLER_ID) {
            selection = FootballerTable.FOOTBALLER_ID + " = " + uri.getLastPathSegment();
        }
        //The DESC is used to alphabetically order the results from the database
        return mDB.query(FootballerTable.TABLE_FOOTBALLERS, FootballerTable.ALL_COLS, selection, null, null,
                null, FootballerTable.FOOTBALLER_NAME + " DESC");
    }

    /* this method was a hack to do a raw query as I was having trouble executing a
    query with a where clause however

    @Nullable
    public static Cursor queryHack(String footballer_id) {

       return mDB.rawQuery("SELECT * FROM myFootballers WHERE id=?", new String[] {footballer_id+""});
    }*/

    //gets the uri from this class to use for db queries
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    //db create method
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mDB.insert(FootballerTable.TABLE_FOOTBALLERS, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    //db delete method
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        return mDB.delete(FootballerTable.TABLE_FOOTBALLERS, selection, selectionArgs);
    }

    //db update method
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mDB.update(FootballerTable.TABLE_FOOTBALLERS, values, selection, selectionArgs);
    }
}
