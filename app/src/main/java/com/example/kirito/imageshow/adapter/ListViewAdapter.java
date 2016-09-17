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
import com.example.kirito.imageshow.entity.FileItem;
import com.example.kirito.imageshow.entity.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kirito on 2016/9/16.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<FileItem> items;
    private static final String TAG = "ListViewAdapter";

    public ListViewAdapter(Context mContext, List<FileItem> items) {
        this.mContext = mContext;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem,null);
            holder = new viewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.list_iv);
            holder.tv_count = (TextView) convertView.findViewById(R.id.list_tv_count);
            holder.tv_date = (TextView) convertView.findViewById(R.id.list_tv_date);
            holder.tv_name = (TextView) convertView.findViewById(R.id.list_tv_name);

            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }
        FileItem item = items.get(position);
        Picasso.with(mContext).load("file://"+item.getPath())
                .resize(150,150)
                .centerCrop()
                .into(holder.iv);
        holder.tv_name.setText(item.getName());
        holder.tv_count.setText("num:"+item.getCount());//一定要把int转为String，否则报错
        holder.tv_date.setText(item.getDate());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    class viewHolder{
        ImageView iv;
        TextView tv_name;
        TextView tv_count;
        TextView tv_date;
    }
}
