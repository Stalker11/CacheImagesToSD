package com.olegel.cachepicturessd;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Oleg on 20.09.2017.
 */

public class ResizeImage {
    private ImageView imageView;
    private String link;
    private int containerHeight;
    private int containerWidth;
    private boolean resizeContainer = false;
    private static final String TAG = ResizeImage.class.getSimpleName();

    public ResizeImage(ImageView imageView, String link) {
        this.imageView = imageView;
        this.link = link;
    }
    public void setImageView(){
        containerHeight = imageView.getHeight();
        containerWidth = imageView.getWidth();
        resizeContainer = false;
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
        if(resizeContainer){
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = containerHeight;
            layoutParams.width = containerWidth;
            imageView.setLayoutParams(layoutParams);
        }
        imageView.setImageDrawable(new BitmapDrawable(null, BitmapFactory.decodeFile(link,options)));
    }
    public void setImageView(int width, int height){
        this.containerWidth = width;
        this.containerHeight = height;
        resizeContainer = true;
        getScaledBitmap();
    }
}
