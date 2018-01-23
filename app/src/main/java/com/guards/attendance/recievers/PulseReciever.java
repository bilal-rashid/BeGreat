package com.guards.attendance.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.guards.attendance.models.Packet;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.GsonUtils;

/**
 * Created by Bilal Rashid on 1/21/2018.
 */

public class PulseReciever extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Packet packet = new Packet("1234",true,AppUtils.getDateAndTime());
        AppUtils.sendSMS("03345505421", GsonUtils.toJson(packet));
    }
}
