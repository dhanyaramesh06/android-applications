package com.example.musixmatch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MusixAdapter extends ArrayAdapter<Musixmatch> {
    public MusixAdapter(@NonNull Context context, int resource, @NonNull List<Musixmatch> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Musixmatch musix = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_track = (TextView) convertView.findViewById(R.id.tv_track);
            viewHolder.tv_artist = (TextView) convertView.findViewById(R.id.tv_artist);
            viewHolder.tv_album = (TextView) convertView.findViewById(R.id.tv_album);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.track = (TextView) convertView.findViewById(R.id.track);
            viewHolder.artist = (TextView) convertView.findViewById(R.id.artist);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.album = (TextView) convertView.findViewById(R.id.album);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.track.setText(musix.track_name);
        viewHolder.artist.setText(musix.artist_name);
        viewHolder.album.setText(musix.album_name);
        viewHolder.date.setText(musix.updated_time);
        viewHolder.tv_track.setText("Track:");
        viewHolder.tv_artist.setText("Artist:");
        viewHolder.tv_album.setText("Album:");
        viewHolder.tv_date.setText("Date:");

        return convertView;
    }

    private static class ViewHolder {
        TextView tv_track, tv_album, tv_artist, tv_date;
        TextView track, album, artist, date;
    }
}