package com.ffm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.databinding.FragmentAssignedReportsBinding;
import com.ffm.databinding.FragmentDashboardReportsBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class AssignedReportsFragment extends BaseFragment<FragmentAssignedReportsBinding> {

    public static AssignedReportsFragment newInstance(boolean isSystemApp) {
        Bundle args = new Bundle();
        args.putBoolean("isSystemApp", isSystemApp);
        AssignedReportsFragment fragment = new AssignedReportsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmentAssignedReportsBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_assigned_reports, container, false);
        observeClick(binding.getRoot());
        init();
        listenData();
        //setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void init() {

    }

    private void listenData() {

    }
}
