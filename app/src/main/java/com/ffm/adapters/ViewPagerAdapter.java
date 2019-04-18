package com.ffm.adapters;


import com.ffm.fragment.AssignedReportsFragment;
import com.ffm.fragment.InprogressReportsFragment;
import com.ffm.fragment.ReportsFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence titles[] = {"Assigned", "In Progress", "Completed"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ReportsFragment.newInstance(false);
        } else if (position == 1) {
            return InprogressReportsFragment.newInstance(false);
        } else {
            return InprogressReportsFragment.newInstance(false);
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


}
