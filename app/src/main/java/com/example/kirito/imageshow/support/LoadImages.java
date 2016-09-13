package com.example.kirito.imageshow.support;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kirito.imageshow.entity.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirito on 2016/9/10.
 */
public class LoadImages extends AsyncTask<String,Void,Void>{
    private CallBack callBack;
    private ShowProgress dialog;

    public LoadImages(Context context) {
        dialog = new ShowProgress(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        List<Item> itemList = new OpenImages().openImage(params[0]);
        if (callBack != null){
            callBack.setListItem(itemList);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void mVoid) {
        super.onPostExecute(mVoid);
        dialog.dismiss();
    }

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public interface CallBack{
        void setListItem(List<Item> listItem);
    }


}
