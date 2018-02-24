package com.guards.attendance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.Constants;
import com.guards.attendance.utils.GsonUtils;
import java.util.List;

/**
 * Created by Bilal Rashid on 1/28/2018.
 */

public class GuardDetailsFragment extends Fragment{

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
        database = AppDataBase.getAppDatabase(getContext());
        mPacketList = DatabaseUtils.with(database).getPacketsOfEmployee(mGuard.emp_id);
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
        mPacketsAdapter= new PacketsAdapter();
        mHolder.guardsRecycler.setAdapter(mPacketsAdapter);
    }
    private void populateData(List<Packet> objects) {
        mPacketsAdapter.addAll(objects);
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
