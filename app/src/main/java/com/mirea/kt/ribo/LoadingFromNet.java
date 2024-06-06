package com.mirea.kt.ribo;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.net.URL;

public class LoadingFromNet implements Runnable {
    private String src_name;
    private Drawable drawable;
    public LoadingFromNet(String src_name) {
        this.src_name = src_name;
    }
    public Drawable result(){
        return this.drawable;
    }
    @Override
    public void run() {
        try {
            InputStream is = (InputStream) new URL("https://news.store.rambler.ru/img/" + src_name).getContent();
            drawable = Drawable.createFromStream(is, src_name);
        } catch (Exception e) {
            Log.i("LoadingFromNet error", "Error");
        }
    }
}