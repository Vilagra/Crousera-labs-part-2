package com.example.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Vilagra on 03.04.2017.
 */
public class DisplayPhotoActivity extends Activity{
    private static final String TAG = "DisplayImageActivity";
    public static final String  EXTRA_NAME = "name";
    public static final String  EXTRA_PATH = "path";
    private ImageView mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        Log.i(TAG,"onCreate");
        setContentView(R.layout.activity_photo);

        mBitmap = (ImageView)findViewById(R.id.photo);
        String PhotoName = getIntent().getStringExtra(EXTRA_NAME);
        Log.i(TAG, "displaying fullscreen for selfie " + PhotoName);
        String filePath = getIntent().getStringExtra(EXTRA_PATH);
        setTitle(PhotoName);
        new LoadBitmapTask(this,mBitmap).execute(filePath);
        //setProgressBarIndeterminateVisibility(true);
    }

    static class LoadBitmapTask extends AsyncTask<String, String, Bitmap> {

        private ImageView mImageView;
        private Activity mActivity;

        public LoadBitmapTask(Activity activity,ImageView imageView) {
            mImageView = imageView;
            mActivity = activity;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String selfiePath = params[0];

            return scaleImage(selfiePath);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mImageView.setImageBitmap(result);
            //mActivity.setProgressBarIndeterminateVisibility(false);
            super.onPostExecute(result);
        }

        public Bitmap scaleImage(String selfiePath) {
            Bitmap bm = BitmapUtils.getBitmapFromFile(selfiePath);
            int nh = (int) ( bm.getHeight() * (1024.0 / bm.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bm, 1024, nh, true);
            return scaled;
        }
    }
}
