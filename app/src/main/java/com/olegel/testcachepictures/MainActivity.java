package com.olegel.testcachepictures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.olegel.cachepicturessd.CacheLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ImageView image;
    private OutputStream os;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        CacheLoader cache = new CacheLoader.Builder().setContextBuilder(getBaseContext())
                .setUrl("http://elenergi.ru/wp-content/uploads/2017/09/Cathode-ray-tube-operating-principle.jpg",
                        "MyWebPicture").setInto(image).build();
        cache.setImageFromURL();
    }
       /* Executors.newFixedThreadPool(1).execute(new Runnable() {
            @Override
            public void run() {
                final File file = new File(getExternalFilesDir(null), "FileTwo.jpg");

                try {
                    // Very simple code to copy a picture from the application's
                    // resource into the external file.  Note that this code does
                    // no error checking, and assumes the picture is small (does not
                    // try to copy it in chunks).  Note that if external storage is
                    // not currently mounted this will silently fail.
                    // InputStream is = getAssets().open("chs7.jpg");
                    URL url = new URL("http://elenergi.ru/wp-content/uploads/2017/08/Microns-9200-Series-NVMe-SSD-for-save-11-Tbyte-memory.jpg");
                    HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                    httpConnection.setDoInput(true);
                    httpConnection.connect();
                    Log.d(TAG, "run: " + httpConnection.getResponseCode());
                    InputStream is = httpConnection.getInputStream();
                    Bitmap bit = BitmapFactory.decodeStream(is);
                    os = new FileOutputStream(file);
                    bit.compress(Bitmap.CompressFormat.JPEG, 85, os);
                    //os.write(data);
                    is.close();
                    os.close();
                    httpConnection.disconnect();
                } catch (IOException e) {
                    // Unable to create file, likely because external storage is
                    // not currently mounted.
                    Log.w("ExternalStorage", "Error writing " + file, e);
                }
                hasExternalStoragePrivateFile();

            }
        });
    }
    //Picasso.with().load("http://elenergi.ru/wp-content/uploads/2017/09/electric-track.jpg").into(tar);

    private synchronized void hasExternalStoragePrivateFile() {
        //CacheLoader.Builder().
       Runnable run = new Runnable() {
           @Override
           public void run() {
               File file = new File(getExternalFilesDir(null), "FileTwo.jpg");
               Log.d(TAG, "run100: ");
               if(file != null){
                   Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                   image.setImageDrawable(new BitmapDrawable(null, myBitmap));
                   //image.setImageBitmap(myBitmap);
               }
           }
       };
       new Handler(getMainLooper()).post(run);
    }
    void deleteExternalStoragePrivateFile() {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(getExternalFilesDir(null), "DemoFile.jpg");
        if (file != null) {
            file.delete();
        }
    }*/
}
