package com.ffm.adapters;


import com.ffm.fragment.AssignedReportsFragment;
import com.ffm.fragment.InprogressReportsFragment;
import com.ffm.fragment.ReportsFragment;
import com.ffm.util.IssueStatus;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String titles[] = {IssueStatus.ASSIGNED.getValue(), IssueStatus.IN_PROGRESS.getValue(), IssueStatus.COMPLETED.getValue()};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return ReportsFragment.newInstance(IssueStatus.STARTED.getValue());
        } else {
            return ReportsFragment.newInstance(titles[position]);
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
