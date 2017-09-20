package com.olegel.cachepicturessd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
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
    private HttpURLConnection httpConnection;
    private int compressPercent = 100;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    private CacheLoader(Builder builder) {
        this.context = builder.contextBuilder;
        this.loadUrl = builder.urlBuilder;
        this.pictureName = builder.pictureNameBuilder;
        this.imageView = builder.imageViewBuilder;
        this.compressFormat = builder.compressFormat;
        this.compressPercent = builder.quality;
    }

    /**
     * Method download picture from URL and adding into cache directory
     */
    public void saveImageFromURL() {
        Executors.newFixedThreadPool(1).execute(new Runnable() {
            @Override
            public void run() {
                if (context != null) {
                    OutputStream os = null;
                    InputStream is = null;
                    final File file = new File(context.getExternalFilesDir(null), pictureName);

                    try {
                        URL url = new URL(loadUrl);
                        httpConnection = (HttpURLConnection) url.openConnection();
                        httpConnection.setDoInput(true);
                        httpConnection.connect();
                        is = httpConnection.getInputStream();
                        Bitmap bit = BitmapFactory.decodeStream(is);
                        os = new FileOutputStream(file);
                        bit.compress(compressFormat, compressPercent, os);
                        is.close();
                        os.close();
                        httpConnection.disconnect();
                    } catch (IOException e) {
                        Log.w("ExternalStorage", "Error writing " + file, e);
                    } finally {
                        if (httpConnection != null) {
                            httpConnection.disconnect();
                            try {
                                os.flush();
                                os.close();
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Method download picture from URL and adding into cache directory.
     * It have callback and can return operation state into calling method
     *
     * @param callBack callback
     */
    public void saveImageFromURL(final CacheLoaderCallBack callBack) {
        Executors.newFixedThreadPool(1).execute(new Runnable() {
            @Override
            public void run() {
                if (context != null) {
                    OutputStream os = null;
                    InputStream is = null;
                    final File file = new File(context.getExternalFilesDir(null), pictureName);

                    try {
                        URL url = new URL(loadUrl);
                        httpConnection = (HttpURLConnection) url.openConnection();
                        httpConnection.setDoInput(true);
                        httpConnection.connect();
                        is = httpConnection.getInputStream();
                        Bitmap bit = BitmapFactory.decodeStream(is);
                        os = new FileOutputStream(file);
                        bit.compress(Bitmap.CompressFormat.JPEG, 85, os);
                        is.close();
                        os.close();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess();
                            }
                        };
                        new Handler(Looper.getMainLooper()).post(runnable);
                        httpConnection.disconnect();
                    } catch (final IOException e) {
                        Log.w("ExternalStorage", "Error writing " + file, e);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                callBack.onError(e.getMessage());
                            }
                        };
                        new Handler(Looper.getMainLooper()).post(runnable);
                    } finally {
                        if (httpConnection != null) {
                            httpConnection.disconnect();
                            try {
                                os.flush();
                                os.close();
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
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
    public synchronized void setPictureFromCache() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                File file = new File(context.getExternalFilesDir(null), pictureName);
                if (file != null) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (imageView != null) {
                        //imageView.setImageDrawable(new BitmapDrawable(null, myBitmap));
                        new ResizeImage(imageView,file.getAbsolutePath());
                    }
                }
            }
        };
        new Handler(context.getMainLooper()).post(run);
    }

    /**
     * Delete picture from cache
     */
    public boolean deleteExternalStoragePrivateFile() {
        if (context != null) {
            File file = new File(context.getExternalFilesDir(null), pictureName);
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
        private int quality;
        private Bitmap.CompressFormat compressFormat;

        public Builder setContextBuilder(Context contextBuilder) {
            this.contextBuilder = contextBuilder;
            return this;
        }

        /**
         * Set compress format before saving format into cache
         *
         * @param format parameter for Bitmap
         */
        public Builder setCompressFormat(Bitmap.CompressFormat format) {
            this.compressFormat = format;
            return this;
        }

        /**
         * Set quality picture for saving
         *
         * @param percent percent quality. Can be between only 0 and 100
         */
        public Builder setQuality(int percent) {
            if (percent > 0 || percent < 100) {
                this.quality = percent;
            }
            return this;
        }

        /**
         * Set URL where image will loading and picture name for saving into cache directory
         *
         * @param url         URL picture
         * @param pictureName picture name in cache directory
         */
        public Builder setUrl(String url, String pictureName) {
            this.urlBuilder = url;
            this.pictureNameBuilder = pictureName;
            return this;
        }

        /**
         * When you do not change picture you can download this picture into image view container
         *
         * @param imageView container where will set image
         */
        public Builder setInto(ImageView imageView) {
            this.imageViewBuilder = imageView;
            return this;
        }

        /**
         * If want only get picture from cache or delete it you can set only picture name for
         * searching in cache directory
         *
         * @param pictureName name picture which will search in directory
         */
        public Builder setPictureName(String pictureName) {
            this.pictureNameBuilder = pictureName;
            return this;
        }

        public CacheLoader build() {
            return new CacheLoader(this);
        }
    }
}
