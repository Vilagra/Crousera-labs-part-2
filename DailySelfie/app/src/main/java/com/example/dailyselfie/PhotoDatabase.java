package com.example.dailyselfie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vilagra on 02.04.2017.
 */

public class PhotoDatabase  extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "photo_db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_STATEMENT =
            "CREATE TABLE "+ProviderToPhotoDataBase.TABLE_NAME+ " ("+
                    ProviderToPhotoDataBase.COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ProviderToPhotoDataBase.COLUMN_NAME+ " TEXT, "+
                    ProviderToPhotoDataBase.COLUMN_PATH+ " TEXT "+
                    ")";

    public PhotoDatabase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ProviderToPhotoDataBase.TABLE_NAME);
        onCreate(db);
    }
}
