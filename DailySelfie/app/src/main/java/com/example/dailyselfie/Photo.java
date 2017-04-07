package com.example.dailyselfie;

import android.database.Cursor;
import android.graphics.Bitmap;

/**
 * Created by Vilagra on 02.04.2017.
 */

public class Photo {
    private int _id;
    private String name;
    private String path;
    private Bitmap bmp;

    public static Photo fromCursor(Cursor cursor) {
        Photo photo = new Photo();

        photo.setId(cursor.getInt(cursor.getColumnIndex(ProviderToPhotoDataBase.COLUMN_ID)));
        photo.setPath(cursor.getString(cursor.getColumnIndex(ProviderToPhotoDataBase.COLUMN_PATH)));
        photo.setName(cursor.getString(cursor.getColumnIndex(ProviderToPhotoDataBase.COLUMN_NAME)));
        return photo;
    }

    public int getId() {
        return _id;
    }
    public void setId(int id) {
        this._id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public Bitmap getBmp() {
        return bmp;
    }
    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

/*
    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }
*/

}
