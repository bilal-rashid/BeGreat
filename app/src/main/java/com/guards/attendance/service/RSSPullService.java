package com.guards.attendance.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Bilal Rashid on 3/25/2018.
 */

public class RSSPullService extends IntentService {
    public RSSPullService(){
        super("MyService");
    }
    public RSSPullService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        Log.d("TAAAG","Service called");

    }
}