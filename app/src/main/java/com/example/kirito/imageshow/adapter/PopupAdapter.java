package com.example.kirito.imageshow.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.kirito.imageshow.MainActivity;
import com.example.kirito.imageshow.R;

/**
 * Created by kirito on 2016/9/15.
 */
public class PopupAdapter extends BaseAdapter {
    private Context context;
    private int id;
    private int mode;
    private onCheckListener listener;

    private static final String TAG = "PopupAdapter";
    public PopupAdapter(Context context) {
        this.context = context;
    }

    public PopupAdapter(Context context, int mode, onCheckListener listener) {
        this.context = context;
        this.mode = mode;
        this.listener = listener;
    }

    /**
     * getCount决定显示的是对少组convertView
     * @return
     */
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.popup,null);
            holder = new viewHolder();
            holder.rb_grid = (RadioButton) convertView.findViewById(R.id.rb_grid);
            holder.rb_list = (RadioButton) convertView.findViewById(R.id.rb_list);
            holder.rg = (RadioGroup) convertView.findViewById(R.id.rg);
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }
        final viewHolder finalHolder = holder;
        final viewHolder finalHolder1 = holder;
        if (mode == 0){
            holder.rb_grid.setChecked(true);
        }else if (mode == 1){
            holder.rb_list.setChecked(true);
        }
        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_grid){
                    id = 0;
                }else if (checkedId == R.id.rb_list){
                    id = 1;
                }
                if (listener != null){
                    listener.onChecked(id);
                }
            }
        });
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class viewHolder{
        RadioButton rb_grid;
        RadioButton rb_list;
        RadioGroup rg;
    }

    public void setOnCheckListener(onCheckListener listener){
        this.listener = listener;
    }

    public interface onCheckListener{
        void onChecked(int id);
    }
}
