package com.guards.attendance.utils;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Bilal Rashid on 1/21/2018.
 */

public class AttendanceUtils {
    public static String getCheckinKey(){
        return Constants.CHECKIN+AppUtils.getDate();
    }
    public static String getCheckoutKey(){
        return Constants.CHECKOUT+AppUtils.getDate();
    }
    public static void checkinGuard(Context context){
        PrefUtils.persistBoolean(context,getCheckinKey(),true);
    }
    public static boolean isGuardCheckin(Context context){
        return  PrefUtils.getBoolean(context,getCheckinKey(),false);
    }
    public static void checkoutGuard(Context context){
        PrefUtils.persistBoolean(context,getCheckoutKey(),true);
    }
    public static boolean isGuardCheckout(Context context){
        return  PrefUtils.getBoolean(context,getCheckoutKey(),false);
    }
}
