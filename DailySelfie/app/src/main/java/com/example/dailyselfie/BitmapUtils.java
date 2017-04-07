package com.example.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Vilagra on 02.04.2017.
 */

public class BitmapUtils {

    public static final String APP_DIR = "DailyPhoto/Photo";
    public static String mBitmapStoragePath;


    public static void initStoragePath(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (null != root) {
                    File bitmapStorageDir = new File(root, BitmapUtils.APP_DIR);
                    boolean b=bitmapStorageDir.mkdirs();
                    mBitmapStoragePath = bitmapStorageDir.getCanonicalPath();
                    new File(mBitmapStoragePath+"/1.txt").createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Bitmap getBitmapFromFile(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    public static boolean storeBitmapToFile(Bitmap bitmap, String filePath) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(filePath));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                return false;
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String imageFileName = "photo_" + timeStamp;

        File image = new File(mBitmapStoragePath+"/"+imageFileName+".jpg");
        image.createNewFile();

        return image;
    }
}
