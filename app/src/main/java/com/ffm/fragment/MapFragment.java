package com.ffm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.databinding.FragmetMap1Binding;
import com.ffm.util.Trace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class MapFragment extends BaseFragment<FragmetMap1Binding> {

    private double lat;
    private double lng;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmetMap1Binding) DataBindingUtil.inflate(inflater, R.layout.fragmet_map1, container, false);
        observeClick(binding.getRoot());
        init();
        readArgs();

        //setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void readArgs() {
        if (getArguments() != null) {
            lat = MapFragmentArgs.fromBundle(getArguments()).getLat();
            lng = MapFragmentArgs.fromBundle(getArguments()).getLng();
            Trace.i("Location:" + lat + "," + lng);
        }
    }

    private void init() {
    }
}
