package com.guards.attendance.utils;

import android.content.Context;

import com.guards.attendance.R;
import com.guards.attendance.enumerations.StatusEnum;
import com.guards.attendance.models.Packet;
import com.guards.attendance.toolbox.SmsListener;

/**
 * Created by Bilal Rashid on 1/21/2018.
 */

public class AttendanceUtils {
    public static String getCheckinKey(Context context){
        return LoginUtils.getUser(context).employee_code+LoginUtils.getUser(context).username+Constants.CHECKIN+AppUtils.getDate();
    }
    public static String getCheckoutKey(Context context){
        return LoginUtils.getUser(context).employee_code+LoginUtils.getUser(context).username+Constants.CHECKOUT+AppUtils.getDate();
    }
    public static void checkinGuard(Context context){
        PrefUtils.persistBoolean(context,getCheckinKey(context),true);
    }
    public static boolean isGuardCheckin(Context context){
        return  PrefUtils.getBoolean(context,getCheckinKey(context),false);
    }
    public static void checkoutGuard(Context context){
        PrefUtils.persistBoolean(context,getCheckoutKey(context),true);
    }
    public static boolean isGuardCheckout(Context context){
        return  PrefUtils.getBoolean(context,getCheckoutKey(context),false);
    }
    public static void sendCheckin(Context context,SmsListener listener,String location){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                StatusEnum.CHECKIN.getValue(),AppUtils.getDateAndTime(),location,false);
        AppUtils.sendCheckin(context.getString(R.string.admin_number), GsonUtils.toJson(packet).replace(",\"Id\":0",
                "").replace("\"Id\":0,",""),context,listener);
    }
    public static void sendCheckout(Context context,SmsListener listener,String location){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                StatusEnum.CHECKOUT.getValue(),AppUtils.getDateAndTime(),location,false);
        AppUtils.sendCheckout(context.getString(R.string.admin_number), GsonUtils.toJson(packet).replace(",\"Id\":0",
                "").replace("\"Id\":0,",""),context,listener);
    }
    public static void sendEmergency(Context context,String location,SmsListener listener){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                StatusEnum.EMERGENCY.getValue(),AppUtils.getDateAndTime(),location,false);
        AppUtils.sendSMSwithListener(context.getString(R.string.admin_number), GsonUtils.toJson(packet).replace(",\"Id\":0",
                "").replace("\"Id\":0,",""),context,listener);
    }
    public static void sendResponded(Context context,String location){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                StatusEnum.RESPONSE.getValue(),AppUtils.getDateAndTime(),location,false);
        String text = GsonUtils.toJson(packet).replace(",\"Id\":0",
                "").replace("\"Id\":0,","");
        AppUtils.sendSMS(context.getString(R.string.admin_number), text);
    }
    public static void sendNotResponded(Context context,String location){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                StatusEnum.NO_RESPONSE.getValue(),AppUtils.getDateAndTime(),location,false);
        String text = GsonUtils.toJson(packet).replace(",\"Id\":0",
                "").replace("\"Id\":0,","");
        AppUtils.sendSMS(context.getString(R.string.admin_number), text);
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
    public static void sendSupervisorCheckin(Context context, String location, SmsListener listener){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                StatusEnum.CHECKIN.getValue(),AppUtils.getDateAndTime(),location,true);
        AppUtils.sendCheckin(context.getString(R.string.admin_number),
                GsonUtils.toJson(packet).replace(",\"Id\":0","").replace("\"Id\":0,",""),
                context,listener);
    }
    public static void sendSupervisorCheckout(Context context, String location, SmsListener listener){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                StatusEnum.CHECKOUT.getValue(),AppUtils.getDateAndTime(),location,true);
        AppUtils.sendCheckout(context.getString(R.string.admin_number),
                GsonUtils.toJson(packet).replace(",\"Id\":0","").replace("\"Id\":0,",""),
                context,listener);
    }
    public static void sendLocation(Context context,String location,SmsListener listener){
        Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                StatusEnum.LOCATION.getValue(),AppUtils.getDateAndTime(),location,false);
        String text = GsonUtils.toJson(packet).replace(",\"Id\":0",
                "").replace("\"Id\":0,","");
        AppUtils.sendSMSwithListener(context.getString(R.string.admin_number), text,context,listener);
    }
    public static void sendLocation(Context context,String location){
        if(LoginUtils.isGuardUserLogin(context)){
            Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                    StatusEnum.LOCATION.getValue(),AppUtils.getDateAndTime(),location,false);
            String text = GsonUtils.toJson(packet).replace(",\"Id\":0",
                    "").replace("\"Id\":0,","");
            AppUtils.sendSMS(context.getString(R.string.admin_number), text);
        }else if(LoginUtils.isSupervisorUserLogin(context)){
            Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                    StatusEnum.LOCATION.getValue(),AppUtils.getDateAndTime(),location,true);
            String text = GsonUtils.toJson(packet).replace(",\"Id\":0",
                    "").replace("\"Id\":0,","");
            AppUtils.sendSMS(context.getString(R.string.admin_number), text);
        }
    }
    public static void sendGpsOFF(Context context){
        if(LoginUtils.isGuardUserLogin(context)){
            Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                    StatusEnum.NO_LOCATION.getValue(),AppUtils.getDateAndTime(),"noGps_13.679407_171.080469",false);
            String text = GsonUtils.toJson(packet).replace(",\"Id\":0",
                    "").replace("\"Id\":0,","");
            AppUtils.sendSMS(context.getString(R.string.admin_number), text);
        }else if(LoginUtils.isSupervisorUserLogin(context)){
            Packet packet = new Packet(LoginUtils.getUser(context).username+"_"+LoginUtils.getUser(context).employee_code,
                    StatusEnum.NO_LOCATION.getValue(),AppUtils.getDateAndTime(),"noGps_13.679407_171.080469",true);
            String text = GsonUtils.toJson(packet).replace(",\"Id\":0",
                    "").replace("\"Id\":0,","");
            AppUtils.sendSMS(context.getString(R.string.admin_number), text);
        }
    }
}
