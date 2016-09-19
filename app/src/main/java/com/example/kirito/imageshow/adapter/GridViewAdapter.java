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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirito on 2016/9/10.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Item> itemList;
    private boolean isChecked = false;
    private checkListener listener;
    private List<String> dele_path;

    private static final String TAG = "GridViewAdapter";

    public GridViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public GridViewAdapter(Context context, List<Item> itemList, checkListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
        dele_path = new ArrayList<String>();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.griditem,null);
            holder = new viewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.grid_iv);
            holder.iv_check = (ImageView) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }
        final Item item = itemList.get(position);
        //Log.e(TAG, "getView: path---"+"file://"+item.getPath() );
        //加载缩略图（小图片）不会内存溢出
        Picasso.with(context).load("file://"+item.getPath())
                .resize(150,150)
                .centerCrop()
                .into(holder.iv);
        holder.iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isChecked){
                    holder.iv_check.setImageResource(R.drawable.checked);
                    dele_path.add(item.getPath());
                    Log.e(TAG, "onClick: item path---"+item.getPath() );
                    isChecked = true;
                }else if (isChecked){
                    //设置imageview内容为空
                    holder.iv_check.setImageResource(android.R.color.transparent);
                    dele_path.remove(item.getPath());
                    isChecked = false;
                }

                if (listener != null){
                    listener.setCheck(isChecked,dele_path);
                }
            }
        });
        return convertView;
    }

    class viewHolder{
        ImageView iv;
        ImageView iv_check;
    }

    public void setCheckListener(checkListener listener){
        this.listener = listener;
    }

    public interface checkListener{
        void setCheck(boolean check, List<String> path);
    }
}