package com.ffm.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffm.R;
import com.ffm.databinding.FragmetMap1Binding;
import com.ffm.permission.Permission;
import com.ffm.permission.PermissionUtils;
import com.ffm.util.Trace;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

public class MapFragment extends BaseFragment<FragmetMap1Binding> implements OnMapReadyCallback {

    private double lat;
    private double lng;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmetMap1Binding) DataBindingUtil.inflate(inflater, R.layout.fragmet_map1, container, false);
        observeClick(binding.getRoot());
        init();
        readArgs();
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.onResume();
        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.mapView.getMapAsync(this);
        //setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void readArgs() {
        if (getArguments() != null) {
            lat = getArguments().getDouble("lat");
            lng = getArguments().getDouble("lng");
            Trace.i("Location:" + lat + "," + lng);
        }
    }

    private void init() {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (PermissionUtils.isGranted(context, Permission.FINE_LOCATION)) {
            this.googleMap = googleMap;
            this.googleMap.setMyLocationEnabled(true);
            //To add marker
            LatLng location = new LatLng(lat, lng);
            this.googleMap.addMarker(new MarkerOptions().position(location).title("Title").snippet("Marker Description"));
            // For zooming functionality
            CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
            this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });
        } else {
        }
    }


    public void resume() {
        binding.mapView.onResume();
    }


    public void pause() {
        binding.mapView.onPause();
    }

    public void destroy() {
        binding.mapView.onDestroy();
    }
}
