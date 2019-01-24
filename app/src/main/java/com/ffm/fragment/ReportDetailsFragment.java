package com.ffm.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.activity.HomeActivity;
import com.ffm.databinding.FragmentReportDetailsBinding;
import com.ffm.db.paper.PaperConstants;
import com.ffm.db.paper.PaperDB;
import com.ffm.db.room.entity.Report;
import com.ffm.db.room.viewmodels.ReportListViewModel;
import com.ffm.dialog.CustomerResponseDialog;
import com.ffm.dialog.ImageSelectDialog;
import com.ffm.listener.CustomerResponseListener;
import com.ffm.listener.ImageSelectListener;
import com.ffm.listener.UpdateJobListener;
import com.ffm.permission.AskForPermissionDialog;
import com.ffm.permission.AskForPermissionListener;
import com.ffm.permission.Permission;
import com.ffm.permission.PermissionCallback;
import com.ffm.permission.PermissionUtils;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;
import com.ffm.util.Trace;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class ReportDetailsFragment extends BaseFragment<FragmentReportDetailsBinding> implements OnMapReadyCallback {


    private AskForPermissionDialog askForPermissionDialog;
    private Location lastLocation;
    private final static int IMG_RESULT = 26;
    private final static int CAMERA_REQUEST = 24;
    private final static int UPLOAD_REQUEST = 23;
    private GoogleMap googleMap;
    int complaintId;

    private ReportListViewModel reportsViewModel;
    private ArrayList<Report> reportsList = new ArrayList<>();
    private Report report = new Report();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmentReportDetailsBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_report_details, container, false);
        observeClick(binding.getRoot());
        readArgs();
        requestLocation(false);
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.onResume();
        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.mapView.getMapAsync(this);
        init();
        return binding.getRoot();
    }

    private void readArgs() {
        if (getArguments() != null) {
            complaintId = ReportDetailsFragmentArgs.fromBundle(getArguments()).getComplaintId();
            Trace.i("Complaint ID:" + complaintId);
            listenData();
        }
    }

    private void listenData() {
        reportsViewModel = ViewModelProviders.of(this).get(ReportListViewModel.class);
        reportsViewModel.getReports().observe(this, reportsInfo -> {
            //Todo
            reportsList = (ArrayList<Report>) reportsInfo.getReports();
            if (!reportsList.isEmpty() && complaintId < reportsList.size()) {
                binding.setReport(reportsList.get(complaintId));
                report = reportsList.get(complaintId);
                Trace.i("Report:" + report.toString());
                onMapReady(googleMap);
            }
        });
    }

    private void init() {
        binding.setUpdateListener(jobListener);
        binding.close.setOnClickListener(v -> {
            //Todo handle remove image update to server
            binding.setImage(null);
        });
        binding.setImage(PaperDB.getInstance().getImageBitmap());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (PermissionUtils.isGranted(context, Permission.FINE_LOCATION)) {
            this.googleMap = googleMap;
            this.googleMap.setMyLocationEnabled(true);
            //To add marker
            LatLng location = new LatLng(report.getLat(), report.getLng());
            this.googleMap.addMarker(new MarkerOptions().position(location).title("Title").snippet("Marker Description"));
            // For zooming functionality
            CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
            this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            requestLocation(false);
        }
    }

    public void resume() {
        if (getUserVisibleHint()) {
            hideKeyboard(context);
            if (AppPreference.getInstance().getBoolean(AppPrefConstants.JOB_PIC_UPDATE)) {
                AppPreference.getInstance().remove(AppPrefConstants.JOB_PIC_UPDATE);
                binding.setImage(PaperDB.getInstance().getImageBitmap());
            }
            binding.mapView.onResume();
            attachObservers();
        }
    }


    public void pause() {
        if (askForPermissionDialog != null && askForPermissionDialog.isShowing()) {
            askForPermissionDialog.dismiss();
        }
        binding.mapView.onPause();
       /* if (uiWarningDialog != null && uiWarningDialog.isShowing()) {
            uiWarningDialog.dismiss();
        }*/
    }


    public void destroy() {
        binding.mapView.onDestroy();
    }

    private void attachObservers() {
        if (reportsViewModel != null) {
            reportsViewModel.run(this);
        }
    }

    private UpdateJobListener jobListener = new UpdateJobListener() {
        @Override
        public void onAddCustomerResponse() {
            new CustomerResponseDialog(context, response -> {
                //Todo fetch location and update along with response
                Trace.i("Customer response :" + response);
            }).show();
        }

        @Override
        public void onClickEditPhoto() {
            new ImageSelectDialog(context, PaperDB.getInstance().exists(PaperConstants.PROFILE_PICTURE), new ImageSelectListener() {

                @Override
                public void onClickCamera() {
                    requestPermission(Permission.WRITE_STORAGE, new PermissionCallback() {
                        @Override
                        public void onPermissionResult(boolean granted, boolean neverAsk) {
                            if (granted) {
                                requestCamera();
                            } else {
                                snackBarView(getString(R.string.permissions_denied));
                            }
                        }
                    });
                }
            }).show();
        }
    };

    private void requestCamera() {
        requestPermission(Permission.CAMERA, new PermissionCallback() {
            @Override
            public void onPermissionResult(boolean granted, boolean neverAsk) {
                if (granted) {
                    requestLocation(true);
                } else {
                    if (askForPermissionDialog != null && askForPermissionDialog.isShowing()) {
                        askForPermissionDialog.dismiss();
                    }
                    askForPermissionDialog = new AskForPermissionDialog(context, getString(R.string.Camera_permission_request_text), neverAsk, new AskForPermissionListener() {
                        @Override
                        public void ask() {
                            requestCamera();
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

    private void requestLocation(boolean forCamera) {
        requestPermission(Permission.FINE_LOCATION, new PermissionCallback() {
            @Override
            public void onPermissionResult(boolean granted, boolean neverAsk) {
                if (granted) {
                    //Todo
                    //startScan(ScanConstants.OPEN_CAMERA);
                    if (forCamera) {
                        EasyImage.openCamera(ReportDetailsFragment.this, CAMERA_REQUEST);
                        startApiClient();
                    }
                } else {
                    if (askForPermissionDialog != null && askForPermissionDialog.isShowing()) {
                        askForPermissionDialog.dismiss();
                    }
                    askForPermissionDialog = new AskForPermissionDialog(context, getString(R.string.location_permission_request_text), neverAsk, new AskForPermissionListener() {
                        @Override
                        public void ask() {
                            requestLocation(forCamera);
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

    private void startApiClient() {
        try {
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                lastLocation = location;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPLOAD_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                snackBarView(getString(R.string.successfully_image_saved_to_server));
            } else {
                snackBarView(getString(R.string.failed_try_again));
            }
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, (Activity) context, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                if (imagesFiles.size() > 0) {
                    ((HomeActivity) context).showCropAndUpload(imagesFiles.get(0).getAbsolutePath());
                } else {
                    snackBarView(getString(R.string.failed_try_again));
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(context);
                    if (photoFile != null) {
                        //noinspection ResultOfMethodCallIgnored
                        photoFile.delete();
                    }
                }
            }
        });
    }

}
