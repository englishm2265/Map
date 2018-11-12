package com.example.mengl03.map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.Address;
import android.location.Geocoder;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdate;

import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // screen starts with the savedInstanceState, which will end up being activity_maps.xml
        setContentView(R.layout.activity_maps);

        // retrieve city name from original intent
        Intent originalIntent = getIntent();
        city = originalIntent.getStringExtra(Cities.CITY_KEY);
        if(city == null){
            city = Cities.DEFAULT_CITY;
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

            // default latitude and longitude for city before geocoding city
        double latitude = Cities.DEFAULT_LATITUDE;
        double longitude = Cities.DEFAULT_LONGITUDE;

            // retrieve attraction for city
        Cities cities = MainActivity.cities;    // cities is assigned the cities variable from the MainActivity class
        String attraction  = cities.getAttraction(city);    // modify the assignment of attraction so that a result is returned from the searchClick method that is in MapsActivity for the Ch. 11 pt. 2, assignment

            // retrieve latitude and longitude of city/attraction
        Geocoder coder = new Geocoder(this);
        try {
            // geocode city
            String address = attraction + ", " + city;
            List<Address> addresses = coder.getFromLocationName(address, 1);    // the list
            if (addresses != null) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            } else // geocode failed; use default city

                city = Cities.DEFAULT_CITY;
        } catch(IOException ioe){
            // geocoding failed; use default city, latitude and longitude
            city = Cities.DEFAULT_CITY;
        }

            // update the map
        LatLng cityLocation = new LatLng(latitude, longitude);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(cityLocation, 15.5f);
        mMap.moveCamera(update);    // the mMap object that's assigned googleMap at the top is passed the update object, which will cause it to zoom in at a mag of 15

        MarkerOptions options = new MarkerOptions();
        options.position(cityLocation);
        options.title(attraction);
        options.snippet(Cities.MESSAGE);
        mMap.addMarker(options);

        CircleOptions circleOptions = new CircleOptions().center(cityLocation).radius(500).strokeWidth(10.0f).strokeColor(0xFFFF0000);
        mMap.addCircle(circleOptions);
    }


}
