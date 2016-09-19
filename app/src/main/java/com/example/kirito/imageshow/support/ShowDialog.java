package com.example.kirito.imageshow.support;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by kirito on 2016/9/19.
 */
public class ShowDialog {
    private AlertDialog.Builder builder;

    public ShowDialog(Context context,String title,String mes) {
        builder = new AlertDialog.Builder(context).setTitle(title)
                .setMessage(mes).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    public void show(){
        builder.show();
    }
}
