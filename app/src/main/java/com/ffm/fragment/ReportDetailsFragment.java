package com.ffm.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.ffm.FieldForceApplication;
import com.ffm.R;
import com.ffm.activity.HomeActivity;
import com.ffm.adapters.SpinnerAdapter;
import com.ffm.databinding.FragmentReportDetailsBinding;
import com.ffm.db.paper.PaperConstants;
import com.ffm.db.paper.PaperDB;
import com.ffm.db.room.entity.Complaint;
import com.ffm.db.room.entity.LocationInfo;
import com.ffm.db.room.entity.Report;
import com.ffm.db.room.handlers.DataHandler;
import com.ffm.db.room.viewmodels.ComplaintModel;
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
import com.ffm.util.GsonUtil;
import com.ffm.util.IssueStatus;
import com.ffm.util.Trace;
import com.ffm.viewmodels.GetCustomerLocationImageModel;
import com.ffm.viewmodels.GetIssueHistory;
import com.ffm.viewmodels.UpdateIssueDetailsModel;
import com.ffm.viewmodels.request.ComplaintStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class ReportDetailsFragment extends BaseFragment<FragmentReportDetailsBinding> implements OnMapReadyCallback, AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private AskForPermissionDialog askForPermissionDialog;
    private Location lastLocation;
    private final static int IMG_RESULT = 26;
    private final static int CAMERA_REQUEST = 24;
    private final static int UPLOAD_REQUEST = 23;
    private GoogleMap googleMap;
    int complaintId;
    private FusedLocationProviderClient mFusedLocationClient;
    private ComplaintModel complaintModel;
    private Complaint complaint;
    private ArrayList<String> issueMenu = new ArrayList<>();
    private SpinnerAdapter spinnerAdapter;
    private GoogleApiClient mGoogleApiClient;


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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        init();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        return binding.getRoot();
    }

    private void readArgs() {
        if (getArguments() != null) {
            complaintId = ReportDetailsFragmentArgs.fromBundle(getArguments()).getIssueId();
            Trace.i("Complaint ID:" + complaintId);
            listenData();
        }
    }

    private void listenData() {
        complaintModel = ViewModelProviders.of(this).get(ComplaintModel.class);
        complaintModel.getComplaint().observe(this, complaint -> {
            this.complaint = complaint;
            binding.setComplaint(this.complaint);
            Trace.i("Complaint:" + complaint.toString());
            if (complaint.getIssueStatus().equals(IssueStatus.COMPLETED.getValue()))
                getIssueHistory();
            getCustomerLocationImage();
            onMapReady(googleMap);
        });
    }

    private void getIssueHistory() {
        GetIssueHistory issueHistory = new GetIssueHistory(1);
        issueHistory.run(context, complaintId).getData().observe(this, new Observer<Integer>() {
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

    private void init() {
        binding.setUpdateListener(jobListener);
        binding.close.setOnClickListener(v -> {
            //Todo handle remove image update to server
            binding.setImage(null);
        });
        binding.setImage(PaperDB.getInstance().getImageBitmap());
        binding.imgCall.setOnClickListener(view -> {
            if (complaint != null) {
                String phoneNumber = "tel:" + complaint.getCustomerMobile();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(phoneNumber));
                startActivity(intent);
            }
        });
        binding.updateStatus.setOnClickListener(v -> {
            updateLastLocation();
            MaterialButton button = (MaterialButton) v;
            if (button.getText().equals(getString(R.string.start_job))) {
                //Todo start job and update job report along with location to server
                complaint.setIssueStatus(IssueStatus.STARTED.getValue());
            } else if (button.getText().equals(getString(R.string.complete_job))) {
                complaint.setIssueStatus(IssueStatus.COMPLETED.getValue());
            }
            if (lastLocation != null) {
                complaint.setEmployeeLocation(new LocationInfo(lastLocation.getLatitude(), lastLocation.getLongitude()));
            }
            updateServer();
        });
        binding.cbLocationReached.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //Todo update location to server and update complaint object
            //complaint.set(isChecked);
            complaint.setReachedLocation(isChecked);
            updateServer();
        });
        setUpSpinner();
        requestCall();
    }

    private void setUpSpinner() {
        spinnerAdapter = new SpinnerAdapter(context, issueMenu);
        binding.spinner.setAdapter(spinnerAdapter);
        updateIssueMenu();
    }

    private void updateIssueMenu() {
        issueMenu.clear();
        issueMenu.add("Customer Not available");
        issueMenu.add("Issue resolved");
        spinnerAdapter.setItems(issueMenu);
    }

    private void updateServer() {
        //Todo include server call
        //TOdo update reports from server, now load from json
        ComplaintStatus complaintStatus = new ComplaintStatus();
        complaintStatus.setIssueId(complaint.getIssueID());
        complaintStatus.setDescription(complaint.getDescription());
        complaintStatus.setEmployeeId(complaint.getEmployeeID());
        complaintStatus.setIssueStatus(complaint.getIssueStatus());
        complaintStatus.setLocationInfo(complaint.getEmployeeLocation());
        if (AppPreference.getInstance().getBoolean(AppPrefConstants.JOB_PIC_UPDATE)) {
            complaintStatus.setImagePath(AppPreference.getInstance().getString(AppPrefConstants.ISSUE_PIC_PATH));
        }
        UpdateIssueDetailsModel issueDetailsModel = new UpdateIssueDetailsModel(1);
        issueDetailsModel.run(context, complaintStatus).getData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 1) {
                    Trace.i("Failed");
                    FieldForceApplication.getInstance().showToast("Issue status update failed.");
                } else {
                    Trace.i("Success");
                    AppPreference.getInstance().remove(AppPrefConstants.JOB_PIC_UPDATE);
                    binding.setComplaint(complaint);
                    DataHandler.getInstance().updateComplaintToDb(complaint);
                }
            }
        });

    }

    private void getCustomerLocationImage() {
        if (complaint != null && complaint.getImagePath() != null) {
            GetCustomerLocationImageModel imageModel = new GetCustomerLocationImageModel(1);
            imageModel.run(context, complaint.getImagePath()).getData().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer == 0) {
                        binding.setImage(PaperDB.getInstance().getImageBitmap());
                    }
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (PermissionUtils.isGranted(context, Permission.FINE_LOCATION) && googleMap != null) {
            this.googleMap = googleMap;
            this.googleMap.setMyLocationEnabled(true);
            //To add marker
            if (complaint != null) {
                LatLng location = new LatLng(complaint.getCustomerLocation().getLatitude(), complaint.getCustomerLocation().getLongitude());
                this.googleMap.addMarker(new MarkerOptions().position(location).title("Title").snippet("Marker Description"));
                // For zooming functionality
                CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
                this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Bundle bundle = new Bundle();
                        bundle.putDouble("lat", complaint.getCustomerLocation().getLatitude());
                        bundle.putDouble("lng", complaint.getCustomerLocation().getLongitude());
                        Navigation.findNavController(getActivity(), R.id.home_nav_fragment).navigate(R.id.map_fragment, bundle);
                    }
                });
            }
        } else {
            requestLocation(false);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Trace.i("Location services connected.");
        startApiClient();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Trace.i("Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void resume() {
        if (getUserVisibleHint()) {
            hideKeyboard(context);
            if (AppPreference.getInstance().getBoolean(AppPrefConstants.JOB_PIC_UPDATE)) {
                binding.setImage(PaperDB.getInstance().getImageBitmap());
            }
            binding.mapView.onResume();
            mGoogleApiClient.connect();
            attachObservers();
        }
    }


    public void pause() {
        if (askForPermissionDialog != null && askForPermissionDialog.isShowing()) {
            askForPermissionDialog.dismiss();
        }
        binding.mapView.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
       /* if (uiWarningDialog != null && uiWarningDialog.isShowing()) {
            uiWarningDialog.dismiss();
        }*/
    }


    public void destroy() {
        binding.mapView.onDestroy();
    }

    private void attachObservers() {
        if (complaintModel != null) {
            complaintModel.run(this, complaintId);
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

    private void requestCall() {
        requestPermission(Permission.CALL_PHONE, new PermissionCallback() {
            @Override
            public void onPermissionResult(boolean granted, boolean neverAsk) {
                if (granted) {
                    //
                } else {
                    if (askForPermissionDialog != null && askForPermissionDialog.isShowing()) {
                        askForPermissionDialog.dismiss();
                    }
                    askForPermissionDialog = new AskForPermissionDialog(context, getString(R.string.Call_permission_request_text), neverAsk, new AskForPermissionListener() {
                        @Override
                        public void ask() {
                            requestCall();
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
                    // startApiClient();
                    if (forCamera) {
                        EasyImage.openCamera(ReportDetailsFragment.this, CAMERA_REQUEST);

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
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            updateLastLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void updateLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, location -> {
                    if (location != null) {
                        lastLocation = location;
                        Trace.i("Last location:" + lastLocation.getLongitude() + ", " + lastLocation.getLatitude());
                    }
                });
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Trace.i("itemselected:" + issueMenu.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
