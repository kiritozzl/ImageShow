package com.example.kirito.imageshow.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kirito.imageshow.R;
import com.example.kirito.imageshow.entity.Item;
import com.example.kirito.imageshow.support.LoadImages;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirito on 2016/9/12.
 */
public class PagerFragment extends Fragment {
    private ImageView iv;
    private int id;
    private ArrayList<Item> items;

    private static final String TAG = "PagerFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup group = (ViewGroup) inflater.inflate(R.layout.picture,container,false);
        id = getArguments().getInt("id");
        items = (ArrayList<Item>) getArguments().getSerializable("items");
        iv = (ImageView) group.findViewById(R.id.iv_picture);
        Bitmap bitmap = decodeBitmap(new File(items.get(id).getPath()));
        iv.setImageBitmap(bitmap);
        return group;
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
