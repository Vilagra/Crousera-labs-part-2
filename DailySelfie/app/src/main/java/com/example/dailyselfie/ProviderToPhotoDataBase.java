package com.example.dailyselfie;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Vilagra on 02.04.2017.
 */

public class ProviderToPhotoDataBase extends ContentProvider{

    public static final String AUTHORITY = "com.example.dailyselfie";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY + "/");

    public static final String TABLE_NAME = "photo";

    public static final Uri PHOTO_URI = Uri.withAppendedPath(BASE_URI, TABLE_NAME);


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PATH = "path";


    private static final String TAG = "ContentProvider";

    private PhotoDatabase mHelper;


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = mHelper.getWritableDatabase().insert(
                TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri fullUri = ContentUris.withAppendedId(
                    PHOTO_URI, rowID);
            getContext().getContentResolver().notifyChange(fullUri, null);
            return fullUri;
        }
        throw new SQLException("Failed to add record into" + uri);
    }

    @Override
    public boolean onCreate() {
        mHelper = new PhotoDatabase(getContext());
        return mHelper != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        Cursor cursor = qb.query(mHelper.getWritableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        //Not Implemented
        return 0;
    }

}
