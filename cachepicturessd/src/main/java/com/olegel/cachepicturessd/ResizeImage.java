package com.olegel.cachepicturessd;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

/**
 * Created by Oleg on 20.09.2017.
 */

public class ResizeImage {
    private ImageView imageView;
    private String link;
    private int containerHeight;
    private int containerWidth;
    private static final String TAG = ResizeImage.class.getSimpleName();

    public ResizeImage(ImageView imageView, String link) {
        this.imageView = imageView;
        this.link = link;
        setImageView();
    }
    private void setImageView(){
        containerHeight = 75;//imageView.getHeight();
        containerWidth = 35;//imageView.getWidth();
        getScaledBitmap();
    }
    private void getScaledBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(link, options);
        float fileWidth = options.outWidth;
        float fileHeight = options.outHeight;

        int inSampleSize = 1;
        if (fileHeight > containerHeight || fileWidth > containerWidth) {
            if (fileWidth > containerWidth) {
                if(containerHeight != 0){
                    inSampleSize = Math.round(fileHeight / containerHeight);
                } else {
                    inSampleSize = Math.round(fileHeight / fileHeight);
                }

            } else {
                if (containerWidth != 0){
                    inSampleSize = Math.round(fileWidth / containerWidth);
                }else{
                    inSampleSize = Math.round(fileWidth / fileWidth);
                }

            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        imageView.setImageDrawable(new BitmapDrawable(null, BitmapFactory.decodeFile(link,options)));
    }
}
