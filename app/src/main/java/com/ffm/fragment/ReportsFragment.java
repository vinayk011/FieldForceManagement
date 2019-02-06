package com.ffm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.adapters.ComplaintsAdapter;
import com.ffm.databinding.FragmentReportsBinding;
import com.ffm.db.room.entity.Complaint;
import com.ffm.db.room.handlers.DataHandler;
import com.ffm.db.room.viewmodels.ComplaintsViewModel;
import com.ffm.listener.ListItemListener;
import com.ffm.permission.AskForPermissionDialog;
import com.ffm.permission.AskForPermissionListener;
import com.ffm.permission.Permission;
import com.ffm.permission.PermissionCallback;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;
import com.ffm.util.GsonUtil;
import com.ffm.util.Trace;
import com.ffm.viewmodels.GetComplaintsModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class ReportsFragment extends BaseFragment<FragmentReportsBinding> {
    private ComplaintsViewModel complaintsViewModel;
    private ComplaintsAdapter complaintsAdapter;
    private ArrayList<Complaint> complaints = new ArrayList<>();
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
        complaintsViewModel = ViewModelProviders.of(this).get(ComplaintsViewModel.class);
        complaintsViewModel.getComplaints().observe(this, complaintsList -> {
            //Todo
            complaints = (ArrayList<Complaint>) complaintsList;
            binding.setHasReports(complaints != null && !complaints.isEmpty());
            //Collections.sort(complaints, (d1, d2) -> d1.getType().compareTo(d2.getType()));
            if (complaintsAdapter != null) complaintsAdapter.setReports(complaints, false);
        });
    }

    private void init() {
        setRecyclerView();
        AppPreference.getInstance().putString(AppPrefConstants.USER_PHONE, "8008526853");
        AppPreference.getInstance().putString(AppPrefConstants.USER_ID, "EMP101");
    }

    private void setRecyclerView() {
        try {
            complaintsAdapter = new ComplaintsAdapter(complaints, context, new ListItemListener() {
                @Override
                public void onItemClick(int position) {
                    NavDirections directions = ReportsFragmentDirections.actionReportsFragmentToReportDetailsFragment(complaints.get(position).getIssueID(), getString(R.string.report_stats));
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
        binding.recyclerView.setAdapter(complaintsAdapter);
    }

    public void resume() {
        attachObservers();
        listenData();
        updateReportsFromServer();
    }

    private void attachObservers() {
        if (complaintsViewModel != null) {
            complaintsViewModel.run(this);
        }
    }

    private void updateReportsFromServer() {
        GetComplaintsModel complaintsModel = new GetComplaintsModel(1);
        complaintsModel.run(context, "ALL").getData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 1) {
                    Trace.i("Failed");
                } else {
                    Trace.i("Success");
                }
            }
        });
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
