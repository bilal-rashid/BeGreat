package com.guards.attendance.recievers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.guards.attendance.FrameActivity;
import com.guards.attendance.enumerations.StatusEnum;
import com.guards.attendance.fragments.EmergencyFragment;
import com.guards.attendance.models.Packet;
import com.guards.attendance.toolbox.ObservableObject;
import com.guards.attendance.utils.ActivityUtils;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.AttendanceUtils;
import com.guards.attendance.utils.Constants;
import com.guards.attendance.utils.GsonUtils;
import com.guards.attendance.utils.LoginUtils;

import static android.content.Context.POWER_SERVICE;

/**
 * Created by Bilal Rashid on 2/3/2018.
 */

public class MessageReciever extends BroadcastReceiver {
    Context context;
    String number;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 1000;  /* 1 sec */
    private long FASTEST_INTERVAL = 500; /* 1/2 sec */
    public static int counter = 0;
    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mHandler.removeCallbacks(mRunnable);
                ObservableObject.getInstance().updateValue(new Bundle());

            }catch (Exception e){}

        }
    };
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        this.context = context;
        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            String message = SmsMessage.createFromPdu((byte[]) pdus[0]).getMessageBody();
            if(LoginUtils.isAdminUserLogin(context)){
                if (message.contains("\"" + Constants.UNIQUE_ID_GUARD + "\"") ||
                        message.contains("\"" + Constants.UNIQUE_ID_SUPERVISOR + "\"")) {
                    mHandler = new Handler();
                    mHandler.postDelayed(mRunnable, 1000);
                }
            }
            if (message.contains("\"" + Constants.UNIQUE_ID_GUARD + "\"")) {
                Packet packet = GsonUtils.fromJson(message, Packet.class);
                if (packet.status.equals(StatusEnum.EMERGENCY.getValue())) {
                    if(LoginUtils.isAdminUserLogin(context)) {
                        PowerManager.WakeLock screenLock = ((PowerManager) context.getSystemService(POWER_SERVICE)).newWakeLock(
                                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                        screenLock.acquire(5000);
                        AppUtils.vibrate(context);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString(Constants.GUARD_DATA, message);
                        ActivityUtils.startAlarmActivity(context, FrameActivity.class, EmergencyFragment.class.getName(), bundle2, false);
                    }
                }
            }
            if(LoginUtils.isGuardUserLogin(context) || LoginUtils.isSupervisorUserLogin(context)){
                if(message.contains("abcdefgh")){
                    final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        AttendanceUtils.sendGpsOFF(context);
                    } else {
                        startLocationUpdates();
                    }
                }
            }
        }
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        final LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        counter++;
                        if(counter > 1) {
                            onLocationChanged(locationResult.getLastLocation());
                            LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(this);
                        }
                    }
                },
                Looper.myLooper());
    }
    public void onLocationChanged(Location location) {
        // New location has now been determined
        counter = 0;
        String loc = "location" + "_" +Double.toString(location.getLatitude()) + "_" +
                Double.toString(location.getLongitude());
        AttendanceUtils.sendLocation(context,loc);
    }
}
