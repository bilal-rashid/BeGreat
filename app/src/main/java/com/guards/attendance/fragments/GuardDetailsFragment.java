package com.guards.attendance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.guards.attendance.R;
import com.guards.attendance.adapters.PacketsAdapter;
import com.guards.attendance.models.Guard;
import com.guards.attendance.models.Packet;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.Constants;
import com.guards.attendance.utils.GsonUtils;
import com.guards.attendance.utils.SmsUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Bilal Rashid on 1/28/2018.
 */

public class GuardDetailsFragment extends Fragment{

    private ViewHolder mHolder;
    private Guard mGuard;
    private List<Packet> mPacketList;
    private PacketsAdapter mPacketsAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Attendance",false);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guard_detail, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        manipulateBundle();
        mPacketList = SmsUtils.getGuardPackets(getContext(),mGuard.number);
        if(mPacketList.size() > 0){
            setupRecyclerView();
            populateData(mPacketList);
            mHolder.errorContainer.setVisibility(View.GONE);
        }else {
            mHolder.errorContainer.setVisibility(View.VISIBLE);
        }

    }

    private void manipulateBundle() {
        if (getArguments() != null) {
            mGuard = GsonUtils.fromJson(getArguments().getString(Constants.GUARD_DATA),Guard.class);
        }
    }
    private void setupRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mHolder.guardsRecycler.setLayoutManager(mLayoutManager);
        mPacketsAdapter= new PacketsAdapter();
        mHolder.guardsRecycler.setAdapter(mPacketsAdapter);
    }
    private void populateData(List<Packet> objects) {
        mPacketsAdapter.addAll(objects);
    }

    public static class ViewHolder {

        RecyclerView guardsRecycler;
        LinearLayout errorContainer;

        public ViewHolder(View view) {
            guardsRecycler = (RecyclerView) view.findViewById(R.id.recycler_guards);
            errorContainer = (LinearLayout) view.findViewById(R.id.error_container);
        }
    }

}
