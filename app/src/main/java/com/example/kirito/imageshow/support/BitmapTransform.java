package com.example.kirito.imageshow.support;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by kirito on 2016/9/10.
 */
public class BitmapTransform implements Transformation {
    private int maxWidth;
    private int maxHeight;

    public BitmapTransform(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth,targetHeight;
        double ratio ;
        if (source.getWidth() > source.getHeight()){
            targetWidth = maxWidth;
            ratio = (double)source.getHeight() / (double)source.getWidth();
            targetHeight = (int)(targetWidth * ratio);
        }else {
            targetHeight = maxHeight;
            ratio = (double)source.getWidth() / (double)source.getHeight();
            targetWidth = (int)(targetHeight * ratio);
        }
        Bitmap bitmap = Bitmap.createScaledBitmap(source,targetWidth,targetHeight,false);
        if (bitmap != source){
            source.recycle();
        }
        return bitmap;
    }

    @Override
    public String key() {
        return maxWidth + "x" + maxHeight;
    }
}
