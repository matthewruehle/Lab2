package com.mtruehle.photostreamer;

import android.provider.BaseColumns;

/**
 * Created by matt on 9/27/15.
 */
public class FeedSchema implements BaseColumns {
    public static final String TABLE_NAME = "IMAGE_FEED";
    public static final String URLS_COLUMN = "SAVED_IMAGE_URL";
    public static final String POSITION_COLUMN = "POSITION";
    public static final String TEXT_TYPE = " TEXT,";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," +
                    URLS_COLUMN + TEXT_TYPE + POSITION_COLUMN + INTEGER_TYPE +
                    " )";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
