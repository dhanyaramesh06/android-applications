/*
HW - 3
Group 4:
Chella Archana Kandaswamy - 801085762
Dhanya Ramesh - 801073179
 */

package com.example.musicsearchapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements GetMusicAsync.MusicInterface {

    EditText keyword;
    TextView limit;
    SeekBar limitSeek;
    Button search,reset;
    Switch sort;
    ListView results;
    int seekCount;
    boolean flag = false;
    ProgressDialog progressDialog;
    ArrayList<iTunes> resultList ;
    iTunesAdapter itunesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("iTunes Music Search");
        keyword = findViewById(R.id.keyword);
        limit = findViewById(R.id.limit);
        limitSeek = findViewById(R.id.limitSeek);
        sort = findViewById(R.id.sortSwitch);
        results = findViewById(R.id.lv_results);
        search = findViewById(R.id.bt_search);
        reset = findViewById(R.id.bt_reset);
        results = findViewById(R.id.lv_results);
        limitSeek.setMax(30);
        limitSeek.setProgress(10);
        limit.setText("10");

        limitSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                limit.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        if(isConnected())
        {
            Toast.makeText(MainActivity.this, "Internet Connected", Toast.LENGTH_SHORT).show();
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seekCount = Integer.parseInt(limit.getText().toString());
                    if(seekCount < 10 ) {
                        Toast.makeText(MainActivity.this, "Minimum value of limit should be 10", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                    if(keyword.getText().toString().isEmpty())
                    {
                        Toast.makeText(MainActivity.this, "Enter a search keyword", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }

                    Log.d("demo","flag: "+flag);
                    if(flag != true)
                    {
                        RequestParams params = new RequestParams();
                        params.addParameter("term",keyword.getText().toString());
                        params.addParameter("limit",limit.getText().toString());
                        new GetMusicAsync(params,MainActivity.this).execute("https://itunes.apple.com/search");
                    }
                    flag = false;
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    limitSeek.setProgress(10);
                    limit.setText("10");
                    keyword.setText("");
                    sort.setChecked(true);
                    resultList.clear();
                    Log.d("demo","result list after reset; "+resultList);
                    itunesAdapter.notifyDataSetChanged();
                }
            });

            sort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(resultList != null)
                {
                    if (isChecked)
                    {
                        Log.d("demo", "resultlist in sort listener: " + resultList);
                        Collections.sort(resultList, iTunes.Comparators.DATE);
                    } else
                        {
                            Log.d("demo", "resultlist in sort listener: " + resultList);
                            Collections.sort(resultList, iTunes.Comparators.PRICE);
                        }
                        itunesAdapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(MainActivity.this, "No results found!", Toast.LENGTH_SHORT).show();

                }
                });
        }
    }

    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE))
        {
            return false;
        }
        return true;
    }

    @Override
    public void doPostExecute(ArrayList<iTunes> result)
    {
        Log.d("demo","result in post execute: "+result);
        resultList = (ArrayList<iTunes>)result.clone();
        Log.d("demo","resultlist: "+resultList);

        itunesAdapter = new iTunesAdapter(MainActivity.this, R.layout.listview,resultList);
        progressDialog.dismiss();
        if(resultList !=null) {
            if (sort.isChecked()) {
                Collections.sort(resultList, iTunes.Comparators.DATE);
            } else {
                Collections.sort(resultList, iTunes.Comparators.PRICE);
            }
            results.setAdapter(itunesAdapter);
        }
        else
            Toast.makeText(MainActivity.this, "No results found!", Toast.LENGTH_SHORT).show();


        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DisplayDetails.class);
                intent.putExtra("track",itunesAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void doPreExecute() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMax(seekCount/30*100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void updateProgress(Integer... values)
    {
        progressDialog.setProgress(progressDialog.getProgress()+values[0]);
    }
}
