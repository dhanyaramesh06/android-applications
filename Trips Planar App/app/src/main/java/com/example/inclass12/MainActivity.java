/*
GROUP 4 - INCLASS 12
CHELLA ARCHANA KANDASWAMY - 801085762
DHANYA RAMESH - 801073179
 */

package com.example.inclass12;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements ListAdapter.AdapterInterface {
    EditText city,tripname;
    TextView type,date;
    Button search, save, viewtrip, cancel;
    ListView listView;
    String[] placeType = {"airports","amusement parks","aquarium","car rental","police station","museum","city hall","parking"};
    String API_KEY = "AIzaSyBv8UfdkLimrcGuUT1ENTaJVaB0OWy85Gw";
    String latitude, longitude, mon;
    ArrayList<Place> placesArrayList = new ArrayList<>();
    ArrayList<Place> selectedPlaces = new ArrayList<>();
    ListAdapter adapter;
    Calendar calendar;
    int year, month,dayOfMonth;
    private DatePickerDialog.OnDateSetListener mlistener;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Trip t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Trip Planner");

        city = findViewById(R.id.city);
        type = findViewById(R.id.type);
        search = findViewById(R.id.search_btn);
        save = findViewById(R.id.save_btn);
        cancel = findViewById(R.id.cancel_btn);
        listView = findViewById(R.id.places_list);
        date = findViewById(R.id.date);
        tripname = findViewById(R.id.tripName);
        viewtrip = findViewById(R.id.view_trips_btn);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("trips");

        date.setVisibility(View.INVISIBLE);
        tripname.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);

        type.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose a place").setItems(placeType, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String s = placeType[which];
                        type.setText(s);
                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(city.getText().toString().isEmpty() || type.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Choose a valid input", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("demo","search button on click ");
                    //https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+city.getText().toString()+"&inputtype=textquery&fields=geometry,name&key="+API_KEY+"&type=city_hall"
                    RequestParams params = new RequestParams();
                    params.addParameter("input",city.getText().toString());
                    params.addParameter("inputtype","textquery");
                    params.addParameter("fields","geometry,name");
                    params.addParameter("key",API_KEY);
                    params.addParameter("type","city_hall");
                    new getCityCenterAsync(params).execute("https://maps.googleapis.com/maps/api/place/findplacefromtext/json");
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mlistener,year,month,dayOfMonth);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        mlistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String[] month_name = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                mon = month_name[month];
                String d = mon+" "+dayOfMonth+", "+year;
                date.setText(d);
            }
        };

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripname.getText().toString().isEmpty() || date.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Enter valid trip details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                        Trip trip = new Trip();
                        trip.setKey(myRef.push().getKey());
                        trip.setTrip_name(tripname.getText().toString());
                        trip.setDate(date.getText().toString());
                        trip.setDestination_city(city.getText().toString());
                        trip.setSelectedPlaces(selectedPlaces);
                        myRef.child(trip.getKey()).setValue(trip);
                        Toast.makeText(MainActivity.this, "Trip Listed Saved!", Toast.LENGTH_SHORT).show();
                        reset();
                }
            }
        });

        viewtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TripListActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

    }

    @Override
    public void onclick(int pos) {
        Log.d("demo","in list view on click interface");
        Place p = adapter.getItem(pos);
        Log.d("demo","place selected:\n"+p);
        if(p.isSelected() == true)
            selectedPlaces.add(p);
        else
            selectedPlaces.remove(p);
        Set<Place> set = new HashSet<>(selectedPlaces);
        selectedPlaces.clear();
        selectedPlaces.addAll(set);
        Log.d("demo","List of selected places:\n"+selectedPlaces);
    }

    public class getCityCenterAsync extends AsyncTask<String,Integer,Void>
    {
        RequestParams mparams;
        public getCityCenterAsync(RequestParams mparams)
        {
            this.mparams = mparams;
        }

        @Override
        protected Void doInBackground(String... strings)
        {

            Log.d("demo","in get city center async task ");
            HttpURLConnection connection = null;
            if(isConnected())
            {
                try
                {
                    URL url = new URL(mparams.getEncodedUrl(strings[0]));
                    Log.d("demo","URL: "+url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        Log.d("demo","inside if ");
                        String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                        JSONObject root = new JSONObject(json);
                        JSONArray candidates = root.getJSONArray("candidates");
                        for (int i=0;i<candidates.length();i++)
                        {
                            Log.d("demo","inside for ");
                            JSONObject candidatejson = candidates.getJSONObject(i);
                            JSONObject geometry = candidatejson.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            latitude = location.getString("lat");
                            longitude = location.getString("lng");
                        }
                        Log.d("demo","LATITUDE "+latitude);
                        Log.d("demo","LONGITUDE "+ longitude);
                    }
                    else
                    {
                        Log.d("demo","error");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d("demo","json exception "+e);
                }
            }
            else
            {
                Log.d("demo","Internet not connected");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            RequestParams params = new RequestParams();
            //"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=15000&type="+type.getText().toString()+"&key="+API_KEY
            params.addParameter("location",latitude+","+longitude);
            params.addParameter("radius","15000");
            params.addParameter("type",type.getText().toString());
            params.addParameter("key",API_KEY);
            new getPlacesAsync(params).execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
            date.setVisibility(View.VISIBLE);
            tripname.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }
    }

    public class getPlacesAsync extends AsyncTask<String,Integer,Void>
    {
        RequestParams mparams;

        public getPlacesAsync(RequestParams mparams)
        {
            this.mparams = mparams;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.d("demo","in get places async task ");
            HttpURLConnection connection = null;
            if(isConnected())
            {
                try
                {
                    URL url = new URL(mparams.getEncodedUrl(strings[0]));
                    Log.d("demo","GET PLACE URL: "+url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        Log.d("demo","inside if ");
                        String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                        JSONObject root = new JSONObject(json);
                        JSONArray results = root.getJSONArray("results");
                        for (int i=0;i<results.length();i++)
                        {
                            Log.d("demo","inside for ");
                            JSONObject resultsjson = results.getJSONObject(i);
                            JSONObject geometry = resultsjson.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            Place place = new Place();
                            place.setName(resultsjson.getString("name"));
                            place.setLatitude(location.getString("lat"));
                            place.setLongitude(location.getString("lng"));
                            placesArrayList.add(place);
                        }
                        Log.d("demo","PLACES ARRAY LIST:\n"+placesArrayList);

                    }
                    else
                    {
                        Log.d("demo","error");
                    }

                } catch (MalformedURLException e) {
                    Log.d("demo","url exception "+e);
                } catch (IOException e) {
                    Log.d("demo","io exception "+e);
                } catch (JSONException e) {
                    Log.d("demo","json exception "+e);
                }
            }
            else
            {
                Log.d("demo","Internet not connected");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ListAdapter(getBaseContext(), R.layout.listview, placesArrayList, MainActivity.this);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    public void reset()
    {
        listView.setAdapter(null);
        adapter.notifyDataSetChanged();
        tripname.setText("");
        date.setText("");
        city.setText("");
        type.setText("");
        tripname.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        placesArrayList.clear();
        selectedPlaces.clear();
    }

}
