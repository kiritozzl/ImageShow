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
