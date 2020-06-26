package com.example;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Application sApplication;
    public static final String APLICATION_ADDRESS = "http://192.168.100.4:3000";

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
