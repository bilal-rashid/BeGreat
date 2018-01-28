package com.guards.attendance.fragments;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.guards.attendance.FrameActivity;
import com.guards.attendance.R;
import com.guards.attendance.adapters.GuardAdapter;
import com.guards.attendance.api.ApiClient;
import com.guards.attendance.api.ApiInterface;
import com.guards.attendance.dialog.SimpleDialog;
import com.guards.attendance.models.Guard;
import com.guards.attendance.toolbox.OnItemClickListener;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.ActivityUtils;
import com.guards.attendance.utils.LoginUtils;
import com.guards.attendance.utils.SmsUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bilal Rashid on 1/20/2018.
 */

public class AdminHomeFragment extends Fragment implements View.OnClickListener,OnItemClickListener {

    private ViewHolder mHolder;
    private List<Guard> mGuardList;
    private SimpleDialog mSimpleDialog;
    private GuardAdapter mGuardAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitleAdmin("Admin",true);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        mGuardList = SmsUtils.getAllGuards(getContext());
        if(mGuardList.size() > 0){
            setupRecyclerView();
            populateData(mGuardList);
            mHolder.errorContainer.setVisibility(View.GONE);
        }else {
            mHolder.errorContainer.setVisibility(View.VISIBLE);
        }
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//        HashMap<String,String> map = new HashMap<>();
//        map.put("name","bilal");
//        map.put("number","1234");
//        map.put("ghgg","bilal");
//        map.put("kkj","bilal");
//
//        Call<Object> call = apiService.post(map);
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object>call, Response<Object> response) {
//                Log.d("TAAAG",""+response.body());
//
//            }
//
//            @Override
//            public void onFailure(Call<Object>call, Throwable t) {
//
//            }
//        });
    }
    private void setupRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mHolder.guardsRecycler.setLayoutManager(mLayoutManager);
        mGuardAdapter= new GuardAdapter(this);
        mHolder.guardsRecycler.setAdapter(mGuardAdapter);
    }
    private void populateData(List<Guard> objects) {
        mGuardAdapter.addAll(objects);
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        String number = (String)data;
        Toast.makeText(getContext(),number,Toast.LENGTH_SHORT).show();
    }

    public static class ViewHolder {

        RecyclerView guardsRecycler;
        LinearLayout errorContainer;

        public ViewHolder(View view) {
            guardsRecycler = (RecyclerView) view.findViewById(R.id.recycler_guards);
            errorContainer = (LinearLayout) view.findViewById(R.id.error_container);
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
                                ActivityUtils.startHomeActivity(getContext(), FrameActivity.class,null);
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
                Toast.makeText(getContext(),"Sync",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
