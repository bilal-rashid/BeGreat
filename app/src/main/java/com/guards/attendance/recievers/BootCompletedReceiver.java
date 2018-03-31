package com.guards.attendance.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.AttendanceUtils;
import com.guards.attendance.utils.LoginUtils;

/**
 * Created by Bilal Rashid on 3/31/2018.
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        if(LoginUtils.isGuardUserLogin(context)){
            if(AttendanceUtils.isGuardCheckin(context) &&
                    !AttendanceUtils.isGuardCheckout(context)){
                AppUtils.startPulseAfterBoot(context);
            }
        }else if(LoginUtils.isAdminUserLogin(context)){
            AppUtils.startAdminPulse(context);
        }
    }

}