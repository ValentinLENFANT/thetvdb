package com.vag.tvdbapi.util;

import android.os.Looper;

import com.vag.tvdbapi.BuildConfig;

public class ThreadPreconditions {
    public static void checkOnMainThread() {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                throw new IllegalStateException("This method must be called from the UI thread");
            }
        }
    }
}
