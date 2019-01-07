package com.ffm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.adapters.ReportsAdapter;
import com.ffm.databinding.FragmentReportsBinding;
import com.ffm.db.room.entity.Report;
import com.ffm.db.room.viewmodels.ReportListViewModel;
import com.ffm.listener.ListItemListener;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class ReportsFragment extends BaseFragment<FragmentReportsBinding> {
    private ReportListViewModel reportsViewModel;
    private ReportsAdapter reportsAdapter;
    private ArrayList<Report> reportsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmentReportsBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_reports, container, false);
        observeClick(binding.getRoot());
        init();
        listenData();
        //setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void listenData() {
        reportsViewModel = ViewModelProviders.of(this).get(ReportListViewModel.class);
        reportsViewModel.getReports().observe(this, reportsInfo -> {
            //Todo
            reportsList = (ArrayList<Report>) reportsInfo.getReports();
            binding.setHasReports(reportsList != null && !reportsList.isEmpty());
            Collections.sort(reportsList, (d1, d2) -> d1.getName().compareTo(d2.getName()));
            if (reportsAdapter != null) reportsAdapter.setReports(reportsList, false);
        });
    }

    private void init() {
        setRecyclerView();
    }

    private void setRecyclerView() {
        try {
            reportsAdapter = new ReportsAdapter(reportsList, context, new ListItemListener() {
                @Override
                public void onItemClick(int position) {
                    NavDirections directions = ReportsFragmentDirections.actionReportsFragmentToReportDetailsFragment(reportsList.get(position).getReport(), getString(R.string.report_stats));
                    Navigation.findNavController(getActivity(), R.id.home_nav_fragment).navigate(directions);
                }

                @Override
                public void onChanged(boolean isChanged) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(reportsAdapter);
    }

    public void resume() {
        attachObservers();
        updateReportsFromServer();
    }

    private void attachObservers() {
        if (reportsViewModel != null) {
            reportsViewModel.run(this);
        }
    }

    private void updateReportsFromServer() {
       //TOdo update reports from server, now load from json

    }
}