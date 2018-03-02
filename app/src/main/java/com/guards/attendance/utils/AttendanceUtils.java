package com.guards.attendance.utils;

import android.content.Context;

import com.guards.attendance.R;
import com.guards.attendance.enumerations.StatusEnum;
import com.guards.attendance.models.Packet;

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
    public static void sendCheckin(Context context){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"-"+LoginUtils.getUser(context).employee_code,
                StatusEnum.CHECKIN.getName(),AppUtils.getDateAndTime());
        AppUtils.sendSMS(context.getString(R.string.admin_number), GsonUtils.toJson(packet));
    }
    public static void sendCheckout(Context context){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"-"+LoginUtils.getUser(context).employee_code,
                StatusEnum.CHECKOUT.getName(),AppUtils.getDateAndTime());
        AppUtils.sendSMS(context.getString(R.string.admin_number), GsonUtils.toJson(packet));
    }
    public static void sendEmergency(Context context){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"-"+LoginUtils.getUser(context).employee_code,
                StatusEnum.EMERGENCY.getName(),AppUtils.getDateAndTime());
        AppUtils.sendSMS(context.getString(R.string.admin_number), GsonUtils.toJson(packet));
    }
    public static void sendResponded(Context context){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"-"+LoginUtils.getUser(context).employee_code,
                StatusEnum.RESPONSE.getName(),AppUtils.getDateAndTime());
        AppUtils.sendSMS(context.getString(R.string.admin_number), GsonUtils.toJson(packet));
    }
    public static void sendNotResponded(Context context){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"-"+LoginUtils.getUser(context).employee_code,
                StatusEnum.NO_RESPONSE.getName(),AppUtils.getDateAndTime());
        AppUtils.sendSMS(context.getString(R.string.admin_number), GsonUtils.toJson(packet));
    }
    public static void checkinSupervisor(Context context){
        PrefUtils.persistBoolean(context,Constants.SUPERVISOR_CHECKIN,true);
    }
    public static boolean isSupervisorCheckin(Context context){
        return  PrefUtils.getBoolean(context,Constants.SUPERVISOR_CHECKIN,false);
    }
    public static void checkoutSupervisor(Context context){
        PrefUtils.persistBoolean(context,Constants.SUPERVISOR_CHECKIN,false);
    }
    public static void sendSupervisorCheckin(Context context, String location){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"-"+LoginUtils.getUser(context).employee_code,
                StatusEnum.CHECKIN.getName(),AppUtils.getDateAndTime(),location,true);
        AppUtils.sendSMS(context.getString(R.string.admin_number),
                GsonUtils.toJson(packet).replace(",\"packetId\":0",""),context);
    }
    public static void sendSupervisorCheckout(Context context, String location){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"-"+LoginUtils.getUser(context).employee_code,
                StatusEnum.CHECKOUT.getName(),AppUtils.getDateAndTime(),location,true);
        AppUtils.sendSMS(context.getString(R.string.admin_number),
                GsonUtils.toJson(packet).replace(",\"packetId\":0",""),context);
    }
}
