package com.example.kirito.imageshow.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kirito.imageshow.R;
import com.example.kirito.imageshow.entity.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kirito on 2016/9/10.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Item> itemList;

    private static final String TAG = "GridViewAdapter";

    public GridViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.griditem,null);
            holder = new viewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.grid_iv);
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }
        Item item = itemList.get(position);
        //Log.e(TAG, "getView: path---"+"file://"+item.getPath() );
        //加载缩略图（小图片）不会内存溢出
        Picasso.with(context).load("file://"+item.getPath())
                .resize(150,150)
                .centerCrop()
                .into(holder.iv);
        return convertView;
    }

    class viewHolder{
        ImageView iv;
    }
}
