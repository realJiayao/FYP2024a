package com.example.fyp2024.DB;

import android.graphics.Bitmap;

public class BitmapSingleton {
    private static BitmapSingleton instance;
    private Bitmap bitmap;

    private BitmapSingleton() {
    }

    public static synchronized BitmapSingleton getInstance() {
        if (instance == null) {
            instance = new BitmapSingleton();
        }
        return instance;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
