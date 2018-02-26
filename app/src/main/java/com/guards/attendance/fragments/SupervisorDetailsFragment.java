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
import android.widget.TextView;

import com.guards.attendance.R;
import com.guards.attendance.adapters.PacketsAdapter;
import com.guards.attendance.database.AppDataBase;
import com.guards.attendance.database.DatabaseUtils;
import com.guards.attendance.models.Guard;
import com.guards.attendance.models.Packet;
import com.guards.attendance.toolbox.OnItemClickListener;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.Constants;
import com.guards.attendance.utils.GsonUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Bilal Rashid on 2/26/2018.
 */

public class SupervisorDetailsFragment extends Fragment implements OnItemClickListener{

    private ViewHolder mHolder;
    private Guard mGuard;
    private List<Packet> mPacketList;
    private PacketsAdapter mPacketsAdapter;
    AppDataBase database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Supervisor Attendance",false);
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
        database = AppDataBase.getAppDatabase(getContext());
        mPacketList = DatabaseUtils.with(database).getPacketsOfEmployee(mGuard.emp_id);
        Collections.sort(mPacketList, new Comparator<Packet>() {
            @Override
            public int compare(Packet packet, Packet t1) {
                return t1.compare(packet);
            }
        });
        mHolder.emp_id_text.setText(mGuard.emp_id);
        if(mPacketList.size() > 0){
            setupRecyclerView();
            populateData(mPacketList);
        }else {
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
        mPacketsAdapter= new PacketsAdapter(this);
        mHolder.guardsRecycler.setAdapter(mPacketsAdapter);
    }
    private void populateData(List<Packet> objects) {
        mPacketsAdapter.addAll(objects);
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        Packet packet = (Packet) data;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.GUARD_DATA, GsonUtils.toJson(packet));
        Log.d("TAAAG",""+GsonUtils.toJson(packet));
    }

    public static class ViewHolder {

        RecyclerView guardsRecycler;
        TextView emp_id_text;

        public ViewHolder(View view) {
            guardsRecycler = (RecyclerView) view.findViewById(R.id.recycler_guards);
            emp_id_text = (TextView) view.findViewById(R.id.text_emp_id);
        }
    }

}
