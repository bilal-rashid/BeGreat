package com.guards.attendance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guards.attendance.R;
import com.guards.attendance.adapters.PagerAdapter;
import com.guards.attendance.database.AppDataBase;
import com.guards.attendance.database.DatabaseUtils;
import com.guards.attendance.toolbox.ToolbarListener;
import com.guards.attendance.utils.SmsUtils;

/**
 * Created by Bilal Rashid on 2/25/2018.
 */

public class AdminFragment extends Fragment{
    private ViewHolder mHolder;
    private PagerAdapter pagerAdapter;
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
            ((ToolbarListener) context).setTitle("Admin",true);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_admin, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        database = AppDataBase.getAppDatabase(getContext());
        DatabaseUtils.with(database).addPacketsToDB(SmsUtils.getAllPackets(getContext()));
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
}
