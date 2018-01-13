package com.vag.tvdb.front;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class App extends Application {
    public static final String TVDB_API_KEY = "94009C6E221EE354";
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(getCacheSize()));
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static App getInstance(Context context) {
        return (App) context.getApplicationContext();
    }

    private int getCacheSize() {
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        final int screenBytes = screenWidth * screenHeight * 4;
        return screenBytes * 3;
    }
}
