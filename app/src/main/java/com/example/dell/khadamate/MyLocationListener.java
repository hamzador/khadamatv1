package com.example.dell.khadamate;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener {
    public static double Latitude,Longitude;

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){

            Latitude =location.getLatitude();
            Longitude =location.getLongitude();
            Log.e("Latitude ",""+Latitude);
            Log.e("Longitude ",""+Longitude);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
