/*
GROUP 4: IN - CLASS 11
CHELLA ARCHANA KANDASWAMY - 801085762
DHANYA RAMESH - 801073179
 */

package com.example.inclass11;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location location;
    ArrayList<Loc> locArrayList = null;
    List<LatLng> latLngs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_maps);
        setTitle("Paths Activity");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String json = null;
        try {
            InputStream is = getAssets().open("trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            Log.d("demo","Latitude and Longitude:\n"+json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Gson gson = new Gson();
        location = gson.fromJson(json,Location.class);
        locArrayList = location.getPoints();

        if(locArrayList != null )
        {
            for(Loc points:locArrayList)
            {
                Log.d("demo","Latitude: "+points.getLatitude());
                Log.d("demo","Longitude: "+points.getLongitude());
                latLngs.add(new LatLng(Double.parseDouble(points.getLatitude()),Double.parseDouble(points.getLongitude())));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Polyline polyline = googleMap.addPolyline(new PolylineOptions().clickable(true).addAll(latLngs));

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder latlngboundsbuilder = new LatLngBounds.Builder();
                for(LatLng b:latLngs)
                {
                    latlngboundsbuilder.include(b);
                }
                LatLngBounds bounds = latlngboundsbuilder.build();
                mMap.addMarker(new MarkerOptions().position(latLngs.get(0)).title("Start Location"));
                mMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size()-1)).title("End Location"));
                mMap.setLatLngBoundsForCameraTarget(bounds);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds ,15));
            }
        });

    }
}
