package com.guards.attendance;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.guards.attendance.utils.ActivityUtils;


/**
 * Created by Bilal Rashid on 10/8/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            ActivityUtils.startActivity(SplashActivity.this, FrameActivity.class, true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 1000);

    }

    @Override
    public void onBackPressed() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        super.onBackPressed();
    }

}
