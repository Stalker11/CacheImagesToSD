package com.olegel.cachepicturessd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * Created by Oleg on 09.09.2017.
 */

public class CacheLoader {
    private Context context;
    private String loadUrl;
    private String pictureName;
    private ImageView imageView;

    private CacheLoader(Builder builder) {
        this.context = builder.contextBuilder;
        this.loadUrl = builder.urlBuilder;
        this.pictureName = builder.pictureNameBuilder;
        this.imageView = builder.imageViewBuilder;
    }

    /**
     * Method download picture from URL and adding into cache directory
     */
    public void setImageFromURL() {
        Executors.newFixedThreadPool(1).execute(new Runnable() {
            @Override
            public void run() {
                if (context != null) {
                    OutputStream os;
                    final File file = new File(context.getExternalFilesDir(null), pictureName);

                    try {
                        URL url = new URL(loadUrl);
                        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                        httpConnection.setDoInput(true);
                        httpConnection.connect();
                        InputStream is = httpConnection.getInputStream();
                        Bitmap bit = BitmapFactory.decodeStream(is);
                        os = new FileOutputStream(file);
                        bit.compress(Bitmap.CompressFormat.JPEG, 85, os);
                        is.close();
                        os.close();
                        httpConnection.disconnect();
                    } catch (IOException e) {
                        Log.w("ExternalStorage", "Error writing " + file, e);
                    }
                    hasExternalStoragePrivateFile();
                }
            }
        });
    }

    /**
     * Method load picture from cache and set into image view
     */
    private synchronized void hasExternalStoragePrivateFile() throws NullPointerException {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                File file = new File(context.getExternalFilesDir(null), pictureName);
                if (file != null) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (imageView == null) throw new NullPointerException();
                    imageView.setImageDrawable(new BitmapDrawable(null, myBitmap));
                }
            }
        };
        new Handler(context.getMainLooper()).post(run);
    }

    /**
     * Method return file from storage for changing
     */
    public File getPictureFromCache() {
        if (context != null) {
            File file = new File(context.getExternalFilesDir(null), pictureName);
            return file;
        }
        return null;
    }

    /**
     * Set picture from cache
     */
    public void setPictureFromCache() {
        hasExternalStoragePrivateFile();
    }

    /**
     * Delete picture from cache
     */
    public boolean deleteExternalStoragePrivateFile() {
        if (context != null) {
            File file = new File(context.getExternalFilesDir(null), "DemoFile.jpg");
            if (file != null) {
                file.delete();
                return true;
            }
        }
        return false;
    }

    public static class Builder {
        private Context contextBuilder;
        private String urlBuilder;
        private String pictureNameBuilder;
        private ImageView imageViewBuilder;

        public Builder setContextBuilder(Context contextBuilder) {
            this.contextBuilder = contextBuilder;
            return this;
        }

        public Builder setUrl(String url, String pictureName) {
            this.urlBuilder = url;
            this.pictureNameBuilder = pictureName;
            return this;
        }

        public Builder setInto(ImageView imageView) {
            this.imageViewBuilder = imageView;
            return this;
        }

        public CacheLoader build() {
            return new CacheLoader(this);
        }
    }
}
