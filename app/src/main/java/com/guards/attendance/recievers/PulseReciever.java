package com.guards.attendance.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;


import com.guards.attendance.FrameActivity;
import com.guards.attendance.fragments.AlarmFragment;
import com.guards.attendance.utils.ActivityUtils;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.LoginUtils;

import static android.content.Context.POWER_SERVICE;

/**
 * Created by Bilal Rashid on 1/21/2018.
 */

public class PulseReciever extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAAAG","recieved");
        if(LoginUtils.isGuardUserLogin(context)) {
            PowerManager.WakeLock screenLock = ((PowerManager) context.getSystemService(POWER_SERVICE)).newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire(5000);
            AppUtils.vibrate(context);
            ActivityUtils.startAlarmActivity(context, FrameActivity.class, AlarmFragment.class.getName(), null, false);
        }else if (LoginUtils.isAdminUserLogin(context)){

        }else {
            AppUtils.stopPulse(context);
        }
    }
}
