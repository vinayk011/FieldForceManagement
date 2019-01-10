package com.ffm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.databinding.FragmentReportDetailsBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


public class ReportDetailsFragment extends BaseFragment<FragmentReportDetailsBinding> {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmentReportDetailsBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_report_details, container, false);
        observeClick(binding.getRoot());
        readArgs();
        init();
        return binding.getRoot();
    }

    private void readArgs() {

    }

    private void init(){

    }

}
