package com.example.kirito.imageshow.support;

import android.util.Log;

import com.example.kirito.imageshow.entity.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirito on 2016/9/10.
 */
public class OpenImages {
    private List<Item> items;
    private static final String TAG = "OpenImages";
    public OpenImages() {
        items = new ArrayList<>();
    }

    public List<Item> openImage(String path){
        int i = 0;
        File file = new File(path);
        File [] files = file.listFiles();

        for (File f : files){
            if (!f.isDirectory()){
                String name = f.getName();
                if (name.endsWith(".jpeg") || name.endsWith("jpg")
                        || name.endsWith("png") || name.endsWith("gif")){
                    Item item = new Item();
                    if ((f.length() / 1024) <= 40){
                        continue;
                    }
                    item.setSize(calculateSize(f.length()));
                    item.setPath(f.getAbsolutePath());
                    item.setId(i);
                    //Log.e(TAG, "openImage: path---"+f.getAbsolutePath() );
                    item.setName(name);
                    i++;
                    items.add(item);
                }
            }else if (f.isDirectory()){
                openImage(f.getAbsolutePath());
            }
        }
        return items;
    }

    public String calculateSize(long size){
        String file_size = "";
        if (size <= 1024){
            file_size = size + "B";
        }else if (size > 1024 && size <= 1024 * 1024){
            file_size = size + "KB";
        }else if (size > 1024 * 1024 && size <= 1024 * 1024 * 1024){
            file_size = size + "MB";
        }
        return file_size;
    }
}
