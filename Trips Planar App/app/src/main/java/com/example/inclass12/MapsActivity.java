package com.example.inclass12;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<LatLng> latLngs;
    List<String> place_name;
    TextView trip_name, dest_city, trip_date;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setTitle("Trip Details");

        trip_name = findViewById(R.id.trip_name);
        dest_city = findViewById(R.id.dest_city);
        trip_date = findViewById(R.id.trip_date);
        back = findViewById(R.id.back_btn);

        latLngs = new ArrayList<>();
        place_name = new ArrayList<>();

        if(getIntent()!= null && getIntent().getExtras()!=null)
        {
            Trip trip = (Trip)getIntent().getSerializableExtra("tripobject");
            trip_name.setText(trip.getTrip_name());
            trip_date.setText(trip.getDate());
            dest_city.setText(trip.getDestination_city());
            ArrayList<Place> placeArrayList = trip.getSelectedPlaces();
            for(int i = 0 ; i < placeArrayList.size(); i++)
            {
                Place place = placeArrayList.get(i);
                latLngs.add(new LatLng(Double.parseDouble(place.getLatitude()),Double.parseDouble(place.getLongitude())));
                place_name.add(place.getName());
            }
            Log.d("demo","Lat and Long"+latLngs);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, TripListActivity.class);
                startActivity(intent);
            }
        });
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
                for(int i = 0; i<place_name.size(); i++)
                {
                    mMap.addMarker(new MarkerOptions().position(latLngs.get(i)).title(place_name.get(i)));
                }
                mMap.setLatLngBoundsForCameraTarget(bounds);
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds ,15));
            }
        });
    }
}
