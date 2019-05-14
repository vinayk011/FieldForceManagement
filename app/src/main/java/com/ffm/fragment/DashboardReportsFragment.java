package com.ffm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.adapters.ViewPagerAdapter;
import com.ffm.databinding.FragmentDashboardReportsBinding;
import com.ffm.databinding.FragmentReportsBinding;
import com.ffm.util.Trace;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

public class DashboardReportsFragment extends BaseFragment<FragmentDashboardReportsBinding> {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmentDashboardReportsBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_reports, container, false);
        observeClick(binding.getRoot());
        init();
        listenData();
        //setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void init(){
        Trace.i("");
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        binding.viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        //binding.viewPager.setCurrentItem(0, false);
    }

    private void listenData(){

    }


    public void resume() {
        Trace.i("");
    }
}
