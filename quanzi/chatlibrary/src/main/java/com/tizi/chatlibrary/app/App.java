package com.tizi.chatlibrary.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by qixingchen on 16/2/24.
 */
public class App extends Application {

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
    }
}
