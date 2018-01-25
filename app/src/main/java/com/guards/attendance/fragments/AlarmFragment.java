package com.guards.attendance.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mHandler.removeCallbacks(mRunnable);
                count++;
                if(count > 10){
                    mHolder.proSwipeBtn.showResultIcon(false);
                    AttendanceUtils.sendNotResponded(getContext());
                    mediaPlayer.stop();
                    getActivity().finish();
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
    }
    @Override
    public void onSwipeConfirm() {
        mediaPlayer.stop();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // task success! show TICK icon in ProSwipeButton
                mHolder.proSwipeBtn.showResultIcon(true); // false if task failed
                mHandler.removeCallbacks(mRunnable);
                AttendanceUtils.sendResponded(getContext());
                getActivity().finish();
            }
        }, 1000);
    }

    public static class ViewHolder {

        ProSwipeButton proSwipeBtn;

        public ViewHolder(View view) {
            proSwipeBtn = (ProSwipeButton) view.findViewById(R.id.proswipebutton_main);

        }

    }
}

