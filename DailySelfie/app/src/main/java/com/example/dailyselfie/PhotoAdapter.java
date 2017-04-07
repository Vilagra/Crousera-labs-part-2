package com.example.dailyselfie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vilagra on 02.04.2017.
 */

public class PhotoAdapter extends CursorAdapter {
    private static final String TAG = "SelfieAdapter";

    private static LayoutInflater sLayoutInflater = null;
    private List<Photo> mPhotos = new ArrayList<>();
    private Context mContext;

    private Map<String,Bitmap> mBitmaps = new HashMap<String,Bitmap>();

    static class ViewHolder {
        ImageView image;
        TextView name;
    }

    public PhotoAdapter(Context context) {
        super(context,null,0);
        mContext = context;
        sLayoutInflater = LayoutInflater.from(mContext);

        BitmapUtils.initStoragePath(mContext);
    }

    @Override
    public Object getItem(int position) {
        Log.d(TAG,"requesting selfie at position "+position);
        return mPhotos.get(position);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        Cursor oldCursor = super.swapCursor(newCursor);
        mPhotos.clear();
        if (newCursor !=null) {
            newCursor.moveToFirst();
            while(!newCursor.isAfterLast()) {
                Photo photo = Photo.fromCursor(newCursor);
                mPhotos.add(photo);
                newCursor.moveToNext();
            }
        }

        return oldCursor;
    }

    public void addPhoto(Photo photo) {

        mPhotos.add(photo);

        ContentValues values = new ContentValues();

        values.put(ProviderToPhotoDataBase.COLUMN_NAME, photo.getName());
        values.put(ProviderToPhotoDataBase.COLUMN_PATH, photo.getPath());
        //values.put(SelfieContract.SELFIE_COLUMN_THUMB, photo.getThumbPath());
        //Log.i(TAG,"added selfie "+photo.getName()+" at position"+(mSelfies.size()-1));
        //Log.d(TAG,"selfie at "+mSelfies.get(mSelfies.size()-1)+" is "+mSelfies.get(mSelfies.size()-1).getName());

        mContext.getContentResolver().insert(ProviderToPhotoDataBase.PHOTO_URI,values);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        Bitmap bitmap = null;
        String path = cursor.getString(cursor.getColumnIndex(ProviderToPhotoDataBase.COLUMN_PATH));
        if (mBitmaps.containsKey(path)) {
            bitmap = mBitmaps.get(path);
        } else {
            bitmap = BitmapUtils.getBitmapFromFile(path);
            mBitmaps.put(path, bitmap);
        }
        holder.image.setImageBitmap(bitmap);
        holder.name.setText(
                cursor.getString(cursor.getColumnIndex(ProviderToPhotoDataBase.COLUMN_NAME)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View newView;
        ViewHolder holder = new ViewHolder();

        newView = sLayoutInflater.inflate(R.layout.photo_item, parent,
                false);
        holder.image = (ImageView) newView.findViewById(R.id.photo);
        holder.name = (TextView) newView.findViewById(R.id.selfie_name);

        newView.setTag(holder);

        return newView;
    }

}
