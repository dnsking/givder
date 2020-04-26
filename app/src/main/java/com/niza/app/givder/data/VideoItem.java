package com.niza.app.givder.data;

import android.graphics.Bitmap;

public class VideoItem {
    public String path;
    public String id;
    public String duration;
    public String resolution;
    public String size;
    public Bitmap bitmap;
    public VideoItem(String path, String id, String duration, String resolution, String size){
        this.path = path;
        this.id = id;
        this.duration = duration;
        this.resolution = resolution;
        this.size = size;

    }
    public VideoItem(String id, Bitmap bitmap){
        this.id = id;
        this.bitmap = bitmap;

    }
}
