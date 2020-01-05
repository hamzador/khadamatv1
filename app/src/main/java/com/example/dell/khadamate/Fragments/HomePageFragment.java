package com.example.dell.khadamate.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private CardView mPlumberButton,mMechanicButton
            ,mElectricianButton,mCarpenterButton;
    TextView mNormalUserMail;

    public static LocationManager mLocationManager;
    public static MyLocationListener MyLoc;
    public static boolean gps_enabled = false;
    public static boolean network_enabled = false;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("الصفحة الرئيسية");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);
        MyLoc = new MyLocationListener();
        mLocationManager = (LocationManager) this.getContext().getSystemService(LOCATION_SERVICE);
        gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET},10);
            }
        }
        if (!gps_enabled && !network_enabled) {
            Toast.makeText(getActivity(), "رجاءا فعل خاصية تحديد المواقع", LENGTH_SHORT).show();
        }
        if (gps_enabled)
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,MyLoc);
        if (network_enabled)
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,MyLoc);


        mPlumberButton = v.findViewById(R.id.plumberButton);
        mMechanicButton = v.findViewById(R.id.mechanicButton);
        mElectricianButton = v.findViewById(R.id.electricianButton);
        mCarpenterButton =  v.findViewById(R.id.carpenterButton);
        mNormalUserMail = v.findViewById(R.id.NormalUserMail);
        // mNormalUserMail.setText(""+mAuth.getCurrentUser().getEmail());
        //System.out.println("role "+user.getEmail());
        mPlumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Workers.class).putExtra("Service","بلومبي"));
            }
        });
        mMechanicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Workers.class).putExtra("Service","ميكانيكي"));
            }
        });
        mElectricianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Workers.class).putExtra("Service","كهربائي"));
            }
        });
        mCarpenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Workers.class).putExtra("Service","نجار"));
            }
        });


        return v;
    }



}
