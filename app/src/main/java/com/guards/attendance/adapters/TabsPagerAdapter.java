package com.guards.attendance.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.guards.attendance.fragments.AdminGuardsFragment;
import com.guards.attendance.fragments.GuardHomeFragment;

/**
 * Created by Bilal Rashid on 2/25/2018.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new AdminGuardsFragment();
            case 1:
                // Games fragment activity
                return new GuardHomeFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}