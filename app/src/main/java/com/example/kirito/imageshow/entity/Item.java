package com.example.kirito.imageshow.entity;

import java.io.Serializable;

/**
 * Created by kirito on 2016/9/10.
 */
public class Item implements Serializable{
    private String name;
    private String path;
    private String size;
    private int id;

    private int rescourse;
    private int count;
    private String date;
    private String file_name;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRescourse() {
        return rescourse;
    }

    public void setRescourse(int rescourse) {
        this.rescourse = rescourse;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
