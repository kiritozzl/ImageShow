package com.example.kirito.imageshow.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.kirito.imageshow.R;
import com.example.kirito.imageshow.support.BitmapTransform;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by kirito on 2016/9/10.
 */
public class PictureActivity extends AppCompatActivity {
    private ImageView iv;
    private String path;
    private static final int MAX_WIDTH = 768;
    private static final int MAX_HEIGHT = 1024;

    private static final String TAG = "PictureActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);

        iv = (ImageView) findViewById(R.id.iv_picture);
        path = getIntent().getStringExtra("path");
        int size = (int) Math.ceil(Math.sqrt(MAX_HEIGHT * MAX_WIDTH));
        /*Picasso.with(this).load("file://" + path)
                .transform(new BitmapTransform(MAX_WIDTH,MAX_HEIGHT))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .resize(size,size)
                .centerInside()
                .into(iv);*/
        Bitmap bitmap = decodeBitmap(new File(path));
        iv.setImageBitmap(bitmap);
    }

    /*private Bitmap decodeBitmap(File file){
        try{
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file),null,o);
            final int REQYIRED_SIZE  = 150;
            int scale = 1;
            while (o.outWidth / 2 / scale >= REQYIRED_SIZE && o.outHeight / 2 /scale >= REQYIRED_SIZE){
                scale *= 2;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = scale ;
            options.inBitmap
            Log.e(TAG, "decodeBitmap: scale---"+scale);
            return BitmapFactory.decodeStream(new FileInputStream(file),null,options);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }*/

    private Bitmap decodeBitmap(File file){
        try{
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file),null,o);
            Bitmap currentBitmap = Bitmap.createBitmap(o.outWidth,o.outHeight, Bitmap.Config.ARGB_8888);

            final int REQYIRED_SIZE  = 800;
            int scale = 1;
            while (o.outWidth / 2 / scale >= REQYIRED_SIZE && o.outHeight / 2 /scale >= REQYIRED_SIZE){
                scale *= 2;
            }
            Log.e(TAG, "decodeBitmap: scale---"+scale);
            o.inJustDecodeBounds = false;
            o.inSampleSize = scale;
            o.inMutable = true;
            o.inBitmap = currentBitmap;
            return BitmapFactory.decodeStream(new FileInputStream(file),null,o);
            //return currentBitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
