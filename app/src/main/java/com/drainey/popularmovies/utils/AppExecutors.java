package com.drainey.popularmovies.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



/**
 * Created by david-rainey on 6/18/18.
 * Adopted from Google Udacity Course's To do-List Android Architecture Lesson09B code.
 * @https://github.com/udacity/ud851-Exercises
 */

public class AppExecutors {
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIOExecutor;
    private final Executor mainThreadExecutor;
    private final Executor networkIOExecutor;

    private AppExecutors(Executor diskIOExecutor, Executor networkIOExecutor, Executor mainThreadExecutor){
        this.diskIOExecutor = diskIOExecutor;
        this.networkIOExecutor = networkIOExecutor;
        this.mainThreadExecutor = mainThreadExecutor;
    }

    public static AppExecutors getInstance(){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO(){
        return diskIOExecutor;
    }

    public Executor mainThread(){
        return mainThreadExecutor;
    }

    public Executor networkIO(){
        return networkIOExecutor;
    }

    private static class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}
