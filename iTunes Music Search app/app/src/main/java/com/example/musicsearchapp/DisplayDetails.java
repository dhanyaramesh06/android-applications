package com.example.musicsearchapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DisplayDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);
        setTitle("iTunes Music Search");
        TextView track = findViewById(R.id.track);
        TextView genre = findViewById(R.id.genre);
        TextView artist = findViewById(R.id.artist);
        TextView album = findViewById(R.id.album);
        TextView trackPrice = findViewById(R.id.trackPrice);
        TextView albumPrice = findViewById(R.id.albumPrice);
        ImageView img = findViewById(R.id.img);
        Button finish = findViewById(R.id.bt_finish);

        try {
            if (getIntent() != null && getIntent().getExtras() != null) {
                iTunes itrack = (iTunes) getIntent().getExtras().getSerializable("track");
                track.setText(itrack.trackName);
                genre.setText(itrack.genre);
                artist.setText(itrack.artist);
                album.setText(itrack.album);
                trackPrice.setText(itrack.trackPrice+" $");
                albumPrice.setText(itrack.albumPrice+" $");
                Picasso.get().load(itrack.urlToImage).resize(100,100).centerCrop().into(img);
            }
        } catch (Exception e) {
            Log.d("demo", e + "");
        }

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
