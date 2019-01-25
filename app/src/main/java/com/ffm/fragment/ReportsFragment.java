package com.ffm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.adapters.ReportsAdapter;
import com.ffm.databinding.FragmentReportsBinding;
import com.ffm.db.room.entity.Report;
import com.ffm.db.room.handlers.DataHandler;
import com.ffm.db.room.viewmodels.ReportListViewModel;
import com.ffm.listener.ListItemListener;
import com.ffm.permission.AskForPermissionDialog;
import com.ffm.permission.AskForPermissionListener;
import com.ffm.permission.Permission;
import com.ffm.permission.PermissionCallback;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;
import com.ffm.util.GsonUtil;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ReportsFragment extends BaseFragment<FragmentReportsBinding> {
    private ReportListViewModel reportsViewModel;
    private ReportsAdapter reportsAdapter;
    private ArrayList<Report> reportsList = new ArrayList<>();
    private AskForPermissionDialog askForPermissionDialog;

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
            Collections.sort(reportsList, (d1, d2) -> d1.getType().compareTo(d2.getType()));
            if (reportsAdapter != null) reportsAdapter.setReports(reportsList, false);
        });
    }

    private void init() {
        setRecyclerView();
        AppPreference.getInstance().putString(AppPrefConstants.USER_PHONE, "8008526853");
    }

    private void setRecyclerView() {
        try {
            reportsAdapter = new ReportsAdapter(reportsList, context, new ListItemListener() {
                @Override
                public void onItemClick(int position) {
                    NavDirections directions = ReportsFragmentDirections.actionReportsFragmentToReportDetailsFragment(reportsList.get(position).getComplaintId(), getString(R.string.report_stats));
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
        if (!AppPreference.getInstance().getBoolean(AppPrefConstants.JSON_LOADED))
            DataHandler.getInstance().addReportsToDb(GsonUtil.readReportsJSONFile(context));
        listenData();
    }

    private void requestLocation() {
        requestPermission(Permission.FINE_LOCATION, new PermissionCallback() {
            @Override
            public void onPermissionResult(boolean granted, boolean neverAsk) {
                if (granted) {
                    //Todo

                } else {
                    if (askForPermissionDialog != null && askForPermissionDialog.isShowing()) {
                        askForPermissionDialog.dismiss();
                    }
                    askForPermissionDialog = new AskForPermissionDialog(context, getString(R.string.location_permission_request_text), neverAsk, new AskForPermissionListener() {
                        @Override
                        public void ask() {
                            requestLocation();
                        }

                        @Override
                        public void deny() {
                        }
                    });
                    askForPermissionDialog.show();
                }
            }
        });
    }
}
