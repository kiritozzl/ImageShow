package com.example.kirito.imageshow.support;

import android.util.Log;

import com.example.kirito.imageshow.entity.FileItem;
import com.example.kirito.imageshow.entity.Item;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kirito on 2016/9/10.
 */
public class OpenImages {
    private List<Item> items;
    private List<FileItem> fileitems;
    private static final String TAG = "OpenImages";

    public OpenImages() {
        items = new ArrayList<>();
        fileitems = new ArrayList<>();
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
                    //过滤小文件
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

    public List<FileItem> openFileImages(String path){
        File f = new File(path);
        File [] fs = f.listFiles();
        int i = 0;

        for (File file : fs){
            if (!file.isDirectory()){
                i++;
                String name = file.getName();

                if (name.endsWith(".jpeg") || name.endsWith("jpg")
                        || name.endsWith("png") || name.endsWith("gif")){

                    if ((file.length() / 1024) <= 40){
                        continue;
                    }

                    if (i == 1){
                        FileItem fileitem = new FileItem();
                        File parent = file.getParentFile();

                        fileitem.setName(parent.getName());
                        fileitem.setParent_path(parent.getAbsolutePath());
                        fileitem.setCount(getFileCount(parent));
                        fileitem.setDate(getLastModifyDate(parent));
                        fileitem.setPath(file.getAbsolutePath());
                        fileitems.add(fileitem);
                    }
                }
            }else if (file.isDirectory()){
                i = 0;
                openFileImages(file.getAbsolutePath());
            }
        }
        return fileitems;
    }

    public String getLastModifyDate(File file){
        File [] fs = file.listFiles();
        long max_date = 100;

        for (File f : fs){
            if (f.lastModified() > max_date){
                max_date = f.lastModified();
            }
        }
        Date date = new Date(max_date);
        String last_modify_date = new SimpleDateFormat("yyyy,MM,dd HH:mm:ss").format(date);
        return last_modify_date;
    }

    public int getFileCount(File file){
        File [] fs = file.listFiles();
        int count = 0;
        for (File f : fs){
            count++;
        }
        return count;
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
