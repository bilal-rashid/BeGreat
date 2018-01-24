package com.guards.attendance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.guards.attendance.utils.AttendanceUtils;

/**
 * Created by Bilal Rashid on 1/24/2018.
 */

public class AlarmFragment extends Fragment implements View.OnClickListener {


    private ViewHolder mHolder;
    private Handler mHandler;
    public int count;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mHandler.removeCallbacks(mRunnable);
                count++;
                if(count > 10){
                    mHolder.button.setEnabled(false);
                    AttendanceUtils.sendNotResponded(getContext());
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        mHolder.button.setOnClickListener(this);
        count = 0;
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 500);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbar.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {

        mHandler.removeCallbacks(mRunnable);
        AttendanceUtils.sendResponded(getContext());
        mHolder.button.setEnabled(false);
        getActivity().finish();
    }

    public static class ViewHolder {

        Button button;

        public ViewHolder(View view) {
            button = (Button) view.findViewById(R.id.button);

        }

    }
}

