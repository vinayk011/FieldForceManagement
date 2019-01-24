package com.ffm.fragment;

import android.Manifest;
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
import com.ffm.dialog.CustomerResponseDialog;
import com.ffm.dialog.ImageSelectDialog;
import com.ffm.listener.CustomerResponseListener;
import com.ffm.listener.ImageSelectListener;
import com.ffm.listener.UpdateJobListener;
import com.ffm.permission.AskForPermissionDialog;
import com.ffm.permission.AskForPermissionListener;
import com.ffm.permission.Permission;
import com.ffm.permission.PermissionCallback;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;
import com.ffm.util.Trace;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class ReportDetailsFragment extends BaseFragment<FragmentReportDetailsBinding> {


    private AskForPermissionDialog askForPermissionDialog;
    private Location lastLocation;
    private final static int IMG_RESULT = 26;
    private final static int CAMERA_REQUEST = 24;
    private final static int UPLOAD_REQUEST = 23;


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

    private void init() {
        binding.setUpdateListener(jobListener);
        binding.close.setOnClickListener(v -> {
            //Todo handle remove image update to server
            binding.setImage(null);
        });
        binding.setImage(PaperDB.getInstance().getImageBitmap());
    }

    public void resume() {
        if (getUserVisibleHint()) {
            hideKeyboard(context);
            if (AppPreference.getInstance().getBoolean(AppPrefConstants.JOB_PIC_UPDATE)) {
                AppPreference.getInstance().remove(AppPrefConstants.JOB_PIC_UPDATE);
                binding.setImage(PaperDB.getInstance().getImageBitmap());
            }
        }
    }


    public void pause() {
        if (askForPermissionDialog != null && askForPermissionDialog.isShowing()) {
            askForPermissionDialog.dismiss();
        }
       /* if (uiWarningDialog != null && uiWarningDialog.isShowing()) {
            uiWarningDialog.dismiss();
        }*/
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
                    requestLocation();
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

    private void requestLocation() {
        requestPermission(Permission.FINE_LOCATION, new PermissionCallback() {
            @Override
            public void onPermissionResult(boolean granted, boolean neverAsk) {
                if (granted) {
                    //Todo
                    //startScan(ScanConstants.OPEN_CAMERA);
                    EasyImage.openCamera(ReportDetailsFragment.this, CAMERA_REQUEST);
                    startApiClient();
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
