package com.guards.attendance.fragments;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guards.attendance.R;
import com.guards.attendance.adapters.PagerAdapter;
import com.guards.attendance.database.AppDataBase;
import com.guards.attendance.database.DatabaseUtils;
import com.guards.attendance.recievers.PulseReciever;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.AppUtils;
import com.guards.attendance.utils.SmsUtils;

/**
 * Created by Bilal Rashid on 2/25/2018.
 */

public class AdminFragment extends Fragment{
    private ViewHolder mHolder;
    private PagerAdapter pagerAdapter;
    AppDataBase database;
    private static final int MY_SMS_REQ_CODE = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Admin",true);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_admin, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppUtils.putDbID(getContext());
        mHolder = new ViewHolder(view);
        database = AppDataBase.getAppDatabase(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_SMS},
                    MY_SMS_REQ_CODE);
        } else {
            DatabaseUtils.with(database).addPacketsToDB(SmsUtils.getAllPackets(getContext()));
        }
        if (mHolder.viewPager != null) {
            setupViewPager(mHolder.viewPager);
        }
        mHolder.tabLayout.setupWithViewPager(mHolder.viewPager);
        mHolder.tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagerAdapter.notifyDataSetChanged();
            }
        });
        Intent ll24 = new Intent(getContext(), PulseReciever.class);
        PendingIntent recurringLl24 = PendingIntent.getBroadcast(getContext(), 0, ll24,
                PendingIntent.FLAG_NO_CREATE);
        if(recurringLl24 != null){
            Log.d("TAAAG","not null");
        }
        else {
            Log.d("TAAAG","null");
            AppUtils.startAdminPulse(getContext());
        }

    }
    private void setupViewPager(ViewPager viewPager) {
        int currentTab = 0;
        currentTab = viewPager.getCurrentItem();

        pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        AdminGuardsFragment adminGuardsFragment = new AdminGuardsFragment();
        pagerAdapter.addFragment(adminGuardsFragment, "Guards");
        AdminSupervisorsFragment adminSupervisorsFragment = new AdminSupervisorsFragment();
        pagerAdapter.addFragment(adminSupervisorsFragment, "Supervisors");
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentTab, false);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class ViewHolder {

        ViewPager viewPager;
        TabLayout tabLayout;
        public ViewHolder(View view) {
            viewPager = (ViewPager) view.findViewById(R.id.viewpager);
            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_SMS_REQ_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DatabaseUtils.with(database).addPacketsToDB(SmsUtils.getAllPackets(getContext()));
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
