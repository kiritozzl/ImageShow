package com.example.kirito.imageshow.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kirito.imageshow.R;
import com.example.kirito.imageshow.entity.Item;
import com.example.kirito.imageshow.support.LoadImages;
import com.example.kirito.imageshow.support.TouchImageView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirito on 2016/9/12.
 */
public class PagerFragment extends Fragment implements View.OnTouchListener{
    private ImageView iv;
    //private TouchImageView iv;
    private int id;
    private ArrayList<Item> items;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;

    private static final String TAG = "PagerFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup group = (ViewGroup) inflater.inflate(R.layout.picture,container,false);
        id = getArguments().getInt("id");
        items = (ArrayList<Item>) getArguments().getSerializable("items");
        iv = (ImageView) group.findViewById(R.id.iv_picture);
        //iv = (TouchImageView) group.findViewById(R.id.iv_picture);
        Bitmap bitmap = decodeBitmap(new File(items.get(id).getPath()));
        iv.setImageBitmap(bitmap);
        iv.setOnTouchListener(this);
        return group;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // handle touch events here
        ImageView view = (ImageView) v;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                //Log.e(TAG, "onTouch: down---" );
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                //Log.e(TAG, "onTouch: pointer down---" );
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                //Log.e(TAG, "onTouch: pointer up---" );
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    //解决一开始设定scaletype 为matrix ，图片最初显示会变形的问题
                    view.setScaleType(ImageView.ScaleType.MATRIX);
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null && event.getPointerCount() == 3) {
                        newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[5];
                        float sx = values[0];
                        float xc = (view.getWidth() / 2) * sx;
                        float yc = (view.getHeight() / 2) * sx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                    }
                }
                //Log.e(TAG, "onTouch: move---" );
                break;
        }
        view.setImageMatrix(matrix);
        return true;
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    /**
     * 以这种方法向PagerFragment传递参数
     * @param id
     * @return
     */
    public static PagerFragment getInstance(int id, ArrayList<Item> items){
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putSerializable("items",items);
        PagerFragment pf = new PagerFragment();
        pf.setArguments(bundle);
        return pf;
    }

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
            //Log.e(TAG, "decodeBitmap: scale---"+scale);
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
