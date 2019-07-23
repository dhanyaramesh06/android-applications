package com.example.musicsearchapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.musicsearchapp.R;
import com.example.musicsearchapp.iTunes;

import java.util.List;

public class iTunesAdapter extends ArrayAdapter<iTunes> {
    public iTunesAdapter(@NonNull Context context, int resource, @NonNull List<iTunes> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        iTunes itunes = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_track = (TextView) convertView.findViewById(R.id.tv_track);
            viewHolder.tv_artist = (TextView) convertView.findViewById(R.id.tv_artist);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.track = (TextView) convertView.findViewById(R.id.track);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.track.setText(itunes.trackName);
        viewHolder.artist.setText(itunes.artist);
        viewHolder.price.setText(itunes.trackPrice+" $");
        viewHolder.date.setText(itunes.date);
        viewHolder.tv_track.setText("Track:");
        viewHolder.tv_artist.setText("Artist:");
        viewHolder.tv_price.setText("Price:");
        viewHolder.tv_date.setText("Date:");

        return convertView;
    }

    private static class ViewHolder
    {
        TextView tv_track,tv_artist,tv_price,tv_date;
        TextView track,artist,price,date;
    }
}
