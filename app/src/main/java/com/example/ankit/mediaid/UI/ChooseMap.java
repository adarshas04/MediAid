package com.example.ankit.mediaid.UI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ankit.mediaid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChooseMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_map);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        map.addMarker(new MarkerOptions().position(getLocation()).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(getLocation()));
    }
    LatLng getLocation() {
        double latti,longi;
        LatLng current_location = null;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ChooseMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 255);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                latti = location.getLatitude();
                longi = location.getLongitude();
                current_location= new LatLng(latti,longi);
                return current_location;
            } else {
                Log.i("yo","Unable to find correct location.");
            }
        }


        return current_location;
    }
}
