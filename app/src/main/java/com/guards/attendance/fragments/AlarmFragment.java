package com.guards.attendance.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.guards.attendance.FrameActivity;
import com.guards.attendance.R;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.ActivityUtils;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.AttendanceUtils;

import in.shadowfax.proswipebutton.ProSwipeButton;

/**
 * Created by Bilal Rashid on 1/24/2018.
 */

public class AlarmFragment extends Fragment implements ProSwipeButton.OnSwipeListener {


    private ViewHolder mHolder;
    private Handler mHandler;
    MediaPlayer mediaPlayer;
    public int count;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 1000;  /* 1 sec */
    private long FASTEST_INTERVAL = 500; /* 1/2 sec */
    public static int counter = 0;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mHandler.removeCallbacks(mRunnable);
                count++;
                if(count > 40){
                    mHolder.proSwipeBtn.showResultIcon(false);
                    final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        AttendanceUtils.sendNotResponded(getContext(),"noGps_13.679407_171.080469");
                    } else {
                        startLocationUpdates(false);
                    }
                    mediaPlayer.stop();
                    Log.d("TAAAG","FINISGED");
                }
                else {
                    mHandler.postDelayed(mRunnable, 1000);
                }
            }catch (Exception e){}

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Alarm",true);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        mHolder.proSwipeBtn.setOnSwipeListener(this);
        count = 0;
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 500);
        AppUtils.IncreaseSound(getContext());
        mediaPlayer = MediaPlayer.create(getContext(), Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.setVolume(100,100);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }
    @Override
    public void onSwipeConfirm() {
        mediaPlayer.stop();
        mHandler.removeCallbacks(mRunnable);
        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AttendanceUtils.sendResponded(getContext(),"noGps_13.679407_171.080469");
        } else {
            startLocationUpdates(true);
        }
    }

    public static class ViewHolder {

        ProSwipeButton proSwipeBtn;

        public ViewHolder(View view) {
            proSwipeBtn = (ProSwipeButton) view.findViewById(R.id.proswipebutton_main);

        }

    }
    protected void startLocationUpdates(final boolean isResponded) {

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
        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        counter++;
                        if(counter > 1) {
                            onLocationChanged(locationResult.getLastLocation(),isResponded);
                            LocationServices.getFusedLocationProviderClient(getContext()).removeLocationUpdates(this);
                        }
                    }
                },
                Looper.myLooper());
    }
    public void onLocationChanged(Location location,boolean isResponded) {
        // New location has now been determined
        counter = 0;
        String loc = "location" + "_" +Double.toString(location.getLatitude()) + "_" +
                Double.toString(location.getLongitude());
        if(isResponded){
            AttendanceUtils.sendResponded(getContext(),loc);
        }else {
            AttendanceUtils.sendNotResponded(getContext(),loc);
        }
        getActivity().finish();
    }
}

