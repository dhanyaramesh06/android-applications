package com.example.inclass12;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripListActivity extends AppCompatActivity {

    ListView triplistview;
    Button back;
    TripAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<Trip> triplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        setTitle("Trip List");

        triplistview = findViewById(R.id.triplistview);
        back = findViewById(R.id.back_btn);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("trips");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                triplist = new ArrayList<>();
                for(DataSnapshot d:dataSnapshot.getChildren())
                {
                    triplist.add(d.getValue(Trip.class));
                    Log.d("demo","Trip name list "+triplist);
                }
                if(triplist.isEmpty())
                {
                    triplistview.setVisibility(View.INVISIBLE);
                    Toast.makeText(getBaseContext(), "No trips to display!", Toast.LENGTH_SHORT).show();
                }
                adapter = new TripAdapter(getBaseContext(),R.layout.triplistview,triplist);
                triplistview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        triplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trip trip = adapter.getItem(position);
                Intent intent = new Intent(TripListActivity.this,MapsActivity.class);
                intent.putExtra("tripobject",trip);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
