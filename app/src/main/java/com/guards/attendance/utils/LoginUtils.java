package com.guards.attendance.utils;

import android.content.Context;
import android.util.Log;

import com.guards.attendance.R;
import com.guards.attendance.models.User;

/**
 * Created by Bilal Rashid on 1/20/2018.
 */

public class LoginUtils {
    public static boolean isAdminUserLogin(Context context) {
        return PrefUtils.getBoolean(context, Constants.USER_ADMIN_lOGIN, false);
    }
    public static boolean isGuardUserLogin(Context context) {
        return PrefUtils.getBoolean(context, Constants.USER_GUARD_LOGIN, false);
    }
    public static boolean isSupervisorUserLogin(Context context) {
        return PrefUtils.getBoolean(context, Constants.USER_SUPERVISOR_LOGIN, false);
    }
    public static void saveUser(Context context, User user) {
        if (user == null)
            return;
        PrefUtils.persistString(context, Constants.USER, GsonUtils.toJson(user));
    }
    public static User getUser(Context context) {
        return GsonUtils.fromJson(PrefUtils.getString(context, Constants.USER), User.class);
    }
    public static void loginAdmin(Context context){
        PrefUtils.persistBoolean(context,Constants.USER_ADMIN_lOGIN,true);
    }
    public static void loginGuard(Context context){
        PrefUtils.persistBoolean(context,Constants.USER_GUARD_LOGIN,true);
    }
    public static void loginSupervisor(Context context){
        PrefUtils.persistBoolean(context,Constants.USER_SUPERVISOR_LOGIN,true);
    }
    public static void logout(Context context){
        PrefUtils.persistBoolean(context,Constants.USER_ADMIN_lOGIN,false);
        PrefUtils.persistBoolean(context,Constants.USER_GUARD_LOGIN,false);
        PrefUtils.persistBoolean(context,Constants.USER_SUPERVISOR_LOGIN,false);
    }
    public static boolean authenticateGuard(Context context, String username, String password){
        User user = getUser(context);
        if (user != null){
            if(user.password.equals(password) && user.username.equals(username)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
    public static boolean authenticateAdmin(Context context, String username, String password){
        String adminUsername = context.getString(R.string.admin_user);
        String adminPassword= context.getString(R.string.admin_password);
        if (adminPassword.equals(password )&& adminUsername.equals(username)){
            return true;
        }
        return false;
    }
    public static boolean authenticateSupervisor(Context context, String username, String password){
        User user = getUser(context);
        if (user != null){
            if(user.password.equals(password) && user.username.equals(username) && user.is_supervisor){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
