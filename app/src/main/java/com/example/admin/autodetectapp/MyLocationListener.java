package com.example.admin.autodetectapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.PipedReader;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 3/21/17.
 */

class MyLocationListener implements LocationListener {
    private String TAG = "MyLocationListener";
    private Context mContext ;
    public MyLocationListener(Context context){
        mContext = context;

    }

    @Override
    public void onLocationChanged(Location loc) {

        Toast.makeText(mContext,"Location changed : Lat: " +
                loc.getLatitude() + " Lng: " + loc.getLongitude(),Toast.LENGTH_LONG).show();

        String longitude = "Longitude: " + loc.getLongitude();
        Log.v(TAG, longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.v(TAG, latitude);

    /*----------to get City-Name from coordinates ------------- */
        String cityName=null;
        Geocoder gcd = new Geocoder(mContext,
                Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(), loc
                    .getLongitude(), 1);
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());
            cityName=addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = longitude+"\n"+latitude +
                "\n\nMy Currrent City is: "+cityName;
        Toast.makeText(mContext,s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.d("Latitude","enabled");
    }

    @Override
    public void onStatusChanged(String provider,
                                int status, Bundle extras) {
        // TODO Auto-generated method stub
        Log.d("Latitude",""+status);
    }
}

