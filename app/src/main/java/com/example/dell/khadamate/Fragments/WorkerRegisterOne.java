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
    private EditText mWorkerFName , mWorkerLName;
    public static String WFirstName, WLastName,WService;

    public static LocationManager mLocationManager;
    public static MyLocationListener MyLoc;
    public static boolean gps_enabled = false;
    public static boolean network_enabled = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragement_worker_register_one,container,false);
        MyLoc = new MyLocationListener();
        mLocationManager = (LocationManager) this.getContext().getSystemService(LOCATION_SERVICE);
        gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (this.getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},10);
            }
        }
        if (!gps_enabled && !network_enabled) {
            Toast.makeText(this.getContext(), "رجاءا فعل خاصية تحديد المواقع", LENGTH_SHORT).show();
        }
        if (gps_enabled)
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,MyLoc);
        if (network_enabled)
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,MyLoc);

        spinner = (Spinner) v.findViewById(R.id.Service);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                R.array.services, R.layout.spinner_item2);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);
        mWorkerFName = v.findViewById(R.id.WorkerFName);
        mWorkerLName = v.findViewById(R.id.WorkerLName);
       final View touchView = this.getActivity().findViewById(R.id.viewPager);
        touchView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                    if(mWorkerLName.getText().toString().trim().isEmpty() || mWorkerFName.getText().toString().trim().isEmpty()
                            || spinner.getSelectedItem().toString().equals("--إختر مهنتك--") ){
                        return true;
                    }else{
                        WFirstName = mWorkerFName.getText().toString();
                        WLastName = mWorkerLName.getText().toString();
                        WService = spinner.getSelectedItem().toString();
                        return false;
                    }

            }
        });

        return v;
    }
}
