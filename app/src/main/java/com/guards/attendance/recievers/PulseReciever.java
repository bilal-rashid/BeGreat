package com.guards.attendance.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.guards.attendance.utils.AppUtils;

/**
 * Created by Bilal Rashid on 1/21/2018.
 */

public class PulseReciever extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        AppUtils.vibrate(context);

    }
}
