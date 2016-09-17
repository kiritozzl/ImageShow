package com.example.kirito.imageshow.support;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kirito.imageshow.entity.FileItem;
import com.example.kirito.imageshow.entity.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirito on 2016/9/10.
 */
public class LoadImages extends AsyncTask<String,Void,Void>{
    private CallBack callBack;
    private ListView listView;
    private ShowProgress dialog;
    private int mode;

    public LoadImages(Context context,int mode) {
        this.mode = mode;
        dialog = new ShowProgress(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        if (mode == 0){
            List<Item> itemList = new OpenImages().openImage(params[0]);
            if (callBack != null){
                callBack.setListItem(itemList);
            }
        }else if (mode == 1){
            List<FileItem> items = new OpenImages().openFileImages(params[0]);
            if (listView != null){
                listView.setListFileItem(items);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void mVoid) {
        super.onPostExecute(mVoid);
        dialog.dismiss();
    }

    public interface ListView{
        void setListFileItem(List<FileItem> listFileItem);
    }

    public void setListListView(ListView listView){
        this.listView = listView;
    }

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public interface CallBack{
        void setListItem(List<Item> listItem);
    }


}
