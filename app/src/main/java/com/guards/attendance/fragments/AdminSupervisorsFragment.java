package com.guards.attendance.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.guards.attendance.FrameActivity;
import com.guards.attendance.R;
import com.guards.attendance.adapters.GuardAdapter;
import com.guards.attendance.api.ApiClient;
import com.guards.attendance.api.ApiInterface;
import com.guards.attendance.database.AppDataBase;
import com.guards.attendance.database.DatabaseUtils;
import com.guards.attendance.dialog.SimpleDialog;
import com.guards.attendance.models.Guard;
import com.guards.attendance.models.Packet;
import com.guards.attendance.models.ResponseModel;
import com.guards.attendance.toolbox.ObservableObject;
import com.guards.attendance.toolbox.OnItemClickListener;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.ActivityUtils;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.Constants;
import com.guards.attendance.utils.GsonUtils;
import com.guards.attendance.utils.LoginUtils;
import com.guards.attendance.utils.SmsUtils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bilal Rashid on 2/26/2018.
 */

public class AdminSupervisorsFragment  extends Fragment implements View.OnClickListener, OnItemClickListener,Observer {

    private ViewHolder mHolder;
    private List<Guard> mGuardList;
    private SimpleDialog mSimpleDialog;
    private GuardAdapter mGuardAdapter;
    AppDataBase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_supervisor_admin, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        mHolder.progressBar.setVisibility(View.GONE);
        database = AppDataBase.getAppDatabase(getContext());
    }

    private static final int MY_WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 3;

    public void getMessagesAndPopulateList() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_SMS},
                    MY_WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            return;
        }
        mGuardList = DatabaseUtils.with(database).getSupervisors();
        if (mGuardList.size() > 0) {
            setupRecyclerView();
            populateData(mGuardList);
            mHolder.errorContainer.setVisibility(View.GONE);
        } else {
            mHolder.errorContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mHolder.guardsRecycler.setLayoutManager(mLayoutManager);
        mGuardAdapter = new GuardAdapter(this);
        mHolder.guardsRecycler.setAdapter(mGuardAdapter);
    }

    private void populateData(List<Guard> objects) {
        mGuardAdapter.addAll(objects);
    }

    @Override
    public void onClick(View view) {

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
    public void onItemClick(View view, Object data, int position) {
        Guard guard = (Guard) data;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.GUARD_DATA, GsonUtils.toJson(guard));
        ActivityUtils.startActivity(getActivity(), FrameActivity.class, SupervisorDetailsFragment.class.getName(), bundle);
    }

    @Override
    public void update(Observable observable, Object o) {
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
        LinearLayout errorContainer;
        ProgressBar progressBar;

        public ViewHolder(View view) {
            guardsRecycler = (RecyclerView) view.findViewById(R.id.recycler_guards);
            errorContainer = (LinearLayout) view.findViewById(R.id.error_container);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.admin_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                mSimpleDialog = new SimpleDialog(getContext(), null, getString(R.string.msg_logout),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                LoginUtils.logout(getContext());
                                ActivityUtils.startHomeActivity(getContext(), FrameActivity.class, null);
                                mSimpleDialog.dismiss();
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();
                return true;
            case R.id.action_sync:
                AppUtils.stopPulse(getContext());
                AppUtils.startAdminPulse(getContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMessagesAndPopulateList();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AppUtils.showSnackBar(getView(), getString(R.string.err_permission_not_granted));
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}