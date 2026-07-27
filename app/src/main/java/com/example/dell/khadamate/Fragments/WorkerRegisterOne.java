package com.example.dell.khadamate.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dell.khadamate.MyLocationListener;
import com.example.dell.khadamate.R;

import static android.content.Context.LOCATION_SERVICE;
import static android.widget.Toast.LENGTH_SHORT;

public class WorkerRegisterOne extends Fragment {
    private Spinner spinner;
    private EditText mWorkerFName, mWorkerLName;
    public static String WFirstName, WLastName, WService;

    public static LocationManager mLocationManager;
    public static MyLocationListener MyLoc;
    public static boolean gps_enabled = false;
    public static boolean network_enabled = false;

    private static final long MIN_TIME_MS = 5000L;
    private static final float MIN_DISTANCE_M = 10f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragement_worker_register_one, container, false);
        startLocationUpdates();

        spinner = v.findViewById(R.id.Service);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(), R.array.services, R.layout.spinner_item2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        mWorkerFName = v.findViewById(R.id.WorkerFName);
        mWorkerLName = v.findViewById(R.id.WorkerLName);
        final View touchView = getActivity().findViewById(R.id.viewPager);
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mWorkerLName.getText().toString().trim().isEmpty()
                        || mWorkerFName.getText().toString().trim().isEmpty()
                        || spinner.getSelectedItem().toString().equals("--إختر مهنتك--")) {
                    return true;
                } else {
                    WFirstName = mWorkerFName.getText().toString().trim();
                    WLastName = mWorkerLName.getText().toString().trim();
                    WService = spinner.getSelectedItem().toString();
                    return false;
                }
            }
        });

        return v;
    }

    private void startLocationUpdates() {
        if (getContext() == null) {
            return;
        }
        MyLoc = new MyLocationListener();
        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (mLocationManager == null) {
            return;
        }
        gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
                return;
            }
        }
        if (!gps_enabled && !network_enabled) {
            Toast.makeText(getContext(), R.string.enable_location, LENGTH_SHORT).show();
            return;
        }
        try {
            if (gps_enabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_MS, MIN_DISTANCE_M, MyLoc);
            }
            if (network_enabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_MS, MIN_DISTANCE_M, MyLoc);
            }
        } catch (SecurityException ignored) {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationManager != null && MyLoc != null) {
            try {
                mLocationManager.removeUpdates(MyLoc);
            } catch (SecurityException ignored) {
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }
}
