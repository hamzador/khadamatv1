package com.example.dell.khadamate.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.khadamate.MyLocationListener;
import com.example.dell.khadamate.R;
import com.example.dell.khadamate.Workers;

import static android.content.Context.LOCATION_SERVICE;
import static android.widget.Toast.LENGTH_SHORT;

public class HomePageFragment extends Fragment {
    private CardView mPlumberButton, mMechanicButton, mElectricianButton, mCarpenterButton;
    TextView mNormalUserMail;

    public static LocationManager mLocationManager;
    public static MyLocationListener MyLoc;
    public static boolean gps_enabled = false;
    public static boolean network_enabled = false;

    private static final long MIN_TIME_MS = 5000L;
    private static final float MIN_DISTANCE_M = 10f;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            getActivity().setTitle(R.string.home_title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);
        startLocationUpdates();

        mPlumberButton = v.findViewById(R.id.plumberButton);
        mMechanicButton = v.findViewById(R.id.mechanicButton);
        mElectricianButton = v.findViewById(R.id.electricianButton);
        mCarpenterButton = v.findViewById(R.id.carpenterButton);
        mNormalUserMail = v.findViewById(R.id.NormalUserMail);

        mPlumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWorkers("بلومبي");
            }
        });
        mMechanicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWorkers("ميكانيكي");
            }
        });
        mElectricianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWorkers("كهربائي");
            }
        });
        mCarpenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWorkers("نجار");
            }
        });

        return v;
    }

    private void openWorkers(String service) {
        startActivity(new Intent(getContext(), Workers.class).putExtra("Service", service));
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
            if (getActivity() != null
                    && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
                return;
            }
        }

        if (!gps_enabled && !network_enabled) {
            Toast.makeText(getActivity(), R.string.enable_location, LENGTH_SHORT).show();
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

    private void stopLocationUpdates() {
        if (mLocationManager != null && MyLoc != null) {
            try {
                mLocationManager.removeUpdates(MyLoc);
            } catch (SecurityException ignored) {
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
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
