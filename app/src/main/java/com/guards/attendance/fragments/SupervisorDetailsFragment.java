package com.guards.attendance.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guards.attendance.MapsActivity;
import com.guards.attendance.R;
import com.guards.attendance.adapters.PacketsAdapter;
import com.guards.attendance.database.AppDataBase;
import com.guards.attendance.database.DatabaseUtils;
import com.guards.attendance.dialog.SimpleDialog;
import com.guards.attendance.models.Guard;
import com.guards.attendance.models.Packet;
import com.guards.attendance.toolbox.ObservableObject;
import com.guards.attendance.toolbox.OnItemClickListener;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.ActivityUtils;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.Constants;
import com.guards.attendance.utils.GsonUtils;
import com.guards.attendance.utils.SmsUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Bilal Rashid on 2/26/2018.
 */

public class SupervisorDetailsFragment extends Fragment implements OnItemClickListener,Observer {

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
        mHolder.emp_id_text.setText(mGuard.emp_id);


    }
    public void getMessagesAndPopulateList() {
        mPacketList = DatabaseUtils.with(database).getPacketsOfEmployee(mGuard.emp_id);
        Collections.sort(mPacketList, new Comparator<Packet>() {
            @Override
            public int compare(Packet packet, Packet t1) {
                return t1.compare(packet);
            }
        });

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
        bundle.putString(Constants.PACKET_DATA, GsonUtils.toJson(packet));
        ActivityUtils.startActivity(getActivity(), MapsActivity.class,bundle);
    }
    @Override
    public void onPause() {
        super.onPause();
        ObservableObject.getInstance().deleteObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_SMS},
                    45);
        } else {
            DatabaseUtils.with(database).addPacketsToDB(SmsUtils.getAllPackets(getContext()));
        }
        getMessagesAndPopulateList();
        ObservableObject.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        Log.d("TAAAG","observable called");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_SMS},
                    45);
        } else {
            DatabaseUtils.with(database).addPacketsToDB(SmsUtils.getAllPackets(getContext()));
        }
        getMessagesAndPopulateList();
    }

    public static class ViewHolder {

        RecyclerView guardsRecycler;
        TextView emp_id_text;

        public ViewHolder(View view) {
            guardsRecycler = (RecyclerView) view.findViewById(R.id.recycler_guards);
            emp_id_text = (TextView) view.findViewById(R.id.text_emp_id);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_get_location:
                AppUtils.sendSMS(mGuard.number,getContext().getString(R.string.generic_msg),getContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
