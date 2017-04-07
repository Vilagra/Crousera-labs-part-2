package com.example.dailyselfie;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = "SelfieActivity";
    private static final int REQ_SNAP_PHOTO = 0;
    private static final long INITIAL_DELAY = 2 * 60 * 1000;
    private static final long REPEAT_DELAY = 2 * 60 * 1000;
    private static final String PHOTO_KEY = "photoPath";

    private PhotoAdapter mAdapter;
    private String mPhotoPath;
    private PendingIntent mAlarmOperation;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPhotoPath = savedInstanceState.getString(PHOTO_KEY);
        }
        mAdapter = new PhotoAdapter(this);

        getListView().setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);


        setAlarm();


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Photo photo = (Photo) mAdapter.getItem(position);
        Intent intent = new Intent(this, DisplayPhotoActivity.class);
        intent.putExtra(DisplayPhotoActivity.EXTRA_NAME, photo.getName());
        intent.putExtra(DisplayPhotoActivity.EXTRA_PATH, photo.getPath());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.take_photo) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                File imageFile = null;
                try {
                    Log.i(TAG, "creating temp file");
                    imageFile = BitmapUtils.createImageFile();
                    mPhotoPath = imageFile.getAbsolutePath();
                    Log.d(TAG, "temp file stored at : " + mPhotoPath);
                } catch (IOException e) {

                    Log.w(TAG, "unable to create image file", e);
                }
                if (imageFile != null) {
                    Log.i(TAG, "starting camera intent to take selfie");
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                    startActivityForResult(cameraIntent, REQ_SNAP_PHOTO);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_SNAP_PHOTO == requestCode) {
            if (resultCode == RESULT_CANCELED) {
                new File(mPhotoPath).delete();
            }
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "processing selfie");
                Photo photo = new Photo();
                photo.setName(new File(mPhotoPath).getName());
                photo.setPath(mPhotoPath);
                mPhotoPath = null;
                mAdapter.addPhoto(photo);
            }
        }

    }


    protected void setAlarm() {
        if (mAlarmOperation == null) {
            mAlarmOperation = PendingIntent.getBroadcast(
                    getApplicationContext(),
                    0,
                    new Intent(getApplicationContext(), AlarmReceiver.class),
                    0);
        }
        AlarmManager alarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        alarm.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INITIAL_DELAY,
                REPEAT_DELAY, mAlarmOperation);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "configuration is changing, saving instance state");
        outState.putString(PHOTO_KEY, mPhotoPath);
    }

    ;


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this, ProviderToPhotoDataBase.PHOTO_URI, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
        mAdapter.swapCursor(newCursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }
}
