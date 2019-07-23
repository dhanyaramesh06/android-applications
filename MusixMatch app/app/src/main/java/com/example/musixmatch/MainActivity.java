package com.example.musixmatch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetMusixmatchAsync.MusicInterface{

    EditText keyword;
    TextView limit;
    SeekBar limitSeek;
    Button search;
    RadioGroup sortby;
    RadioButton track_rating, artist_rating;
    ListView search_result;
    ProgressBar progress;
    int seekCount;
    boolean flag = false;
    ArrayList<Musixmatch> resultList = new ArrayList<Musixmatch>();
    MusixAdapter musixAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MusixMatch Track Search");
        keyword = findViewById(R.id.keyword);
        limit = findViewById(R.id.limit);
        limitSeek = findViewById(R.id.limitSeek);
        track_rating = findViewById(R.id.track_rating);
        artist_rating = findViewById(R.id.artist_rating);
        sortby = findViewById(R.id.radioGroup);
        search_result = findViewById(R.id.lv_results);
        search = findViewById(R.id.bt_search);
        progress = findViewById(R.id.progress);

        limitSeek.setMax(25);
        limitSeek.setProgress(5);
        limit.setText("5");
        track_rating.setChecked(true);
        progress.setVisibility(View.INVISIBLE);

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

        if (isConnected()) {
            Toast.makeText(MainActivity.this, "Internet Connected", Toast.LENGTH_SHORT).show();
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seekCount = Integer.parseInt(limit.getText().toString());
                    if (seekCount < 5) {
                        Toast.makeText(MainActivity.this, "Minimum value of limit should be 5", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                    if (keyword.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter a search keyword", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }

                    Log.d("demo", "flag: " + flag);
                    if (flag != true) {
                        RequestParams params = new RequestParams();
                        params.addParameter("q", keyword.getText().toString());
                        params.addParameter("page_size", limit.getText().toString());
                        if(track_rating.isChecked())
                        {
                            params.addParameter("s_track_rating","desc");
                        }
                        if(artist_rating.isChecked())
                        {
                            params.addParameter("s_artist_rating","desc");
                        }
                        params.addParameter("apikey","981569c96a373cdfe7497fa923b44a6d");
                        new GetMusixmatchAsync(params,MainActivity.this).execute("http://api.musixmatch.com/ws/1.1/track.search");
                    }
                    flag = false;
                }
            });

            artist_rating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(resultList != null)
                    {
                        RequestParams params = new RequestParams();
                        params.addParameter("q", keyword.getText().toString());
                        params.addParameter("page_size", limit.getText().toString());
                        params.addParameter("apikey","981569c96a373cdfe7497fa923b44a6d");
                        if (isChecked)
                        {
                            params.addParameter("s_artist_rating", "desc");
                            new GetMusixmatchAsync(params, MainActivity.this).execute("http://api.musixmatch.com/ws/1.1/track.search");
                        }
                        else
                            {
                            params.addParameter("s_track_rating", "desc");
                            new GetMusixmatchAsync(params, MainActivity.this).execute("http://api.musixmatch.com/ws/1.1/track.search");
                        }
                        musixAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "No results found!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            Log.d("demo","Error in internet connection");
            return false;
        }
        return true;
    }

    @Override
    public void doPostExecute(ArrayList<Musixmatch> result) {
        Log.d("demo","result in post execute: "+result);
        resultList = (ArrayList<Musixmatch>)result.clone();
        Log.d("demo","resultlist: "+resultList);

        musixAdapter = new MusixAdapter(MainActivity.this, R.layout.listview,resultList);
        progress.setVisibility(View.INVISIBLE);
        search_result.setAdapter(musixAdapter);

        search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Musixmatch m = musixAdapter.getItem(position);
                String url = m.track_share_url;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public void doPreExecute() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateProgress(Integer... values) {
        progress.setProgress(progress.getProgress()+values[0]);
    }
}
