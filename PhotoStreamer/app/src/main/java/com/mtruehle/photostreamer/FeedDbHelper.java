package com.mtruehle.photostreamer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by matt on 9/27/15.
 * Schema in FeedSchema.java
 * wheeee.
 */
public class FeedDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Feed.db";
    private SQLiteDatabase writableFeedDb;
    private SQLiteDatabase readableFeedDb;

    public FeedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        writableFeedDb = getWritableDatabase();
        readableFeedDb = getReadableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedSchema.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FeedSchema.SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean writeToDatabase(String urlToSave) {
        ContentValues insertValues = new ContentValues();
        insertValues.put(FeedSchema.URLS_COLUMN, urlToSave);
//        insertValues.put(FeedSchema.POSITION_COLUMN, position);
        try {
            writableFeedDb.insertOrThrow(FeedSchema.TABLE_NAME, null, insertValues);
            return true;
        } catch (Exception ex) {
            Log.e("FeedDbHelper", "Error inserting", ex);
            return false;
        }
    }

    public ArrayList<String> readFeedDb() {
        String[] columnsToQuery = {
                FeedSchema.URLS_COLUMN,
                FeedSchema._ID
        };
        Cursor feedCursor = readableFeedDb.query(FeedSchema.TABLE_NAME, columnsToQuery, null, null, null, null, null);
        ArrayList<String> imageLinks = new ArrayList<String>();
        if (feedCursor.moveToFirst()) { // moves to the first thing. If the cursor is empty, instead the .moveToFirst returns false.
            imageLinks.add(feedCursor.getString(0));
            while (feedCursor.moveToNext()) {
                imageLinks.add(feedCursor.getString(0));
            }
        }
        return imageLinks;
    }

    public boolean deleteEntryFromFeed(String urlToDelete) {

        String selection = FeedSchema.URLS_COLUMN + "= ? ";
        int deleteRows = readableFeedDb.delete(FeedSchema.TABLE_NAME, selection, new String[] {urlToDelete});
        return true;
    }
//
//    public int getNumberOfEntries() {
//        int numberOfEntries = (int) DatabaseUtils.queryNumEntries(readableFeedDb, FeedSchema.URLS_COLUMN);
//        return numberOfEntries;
//    }

}
