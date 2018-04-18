package com.guards.attendance.toolbox;

import android.app.Application;
import android.content.Context;

/**
 * Created by Bilal Rashid on 3/28/2018.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
