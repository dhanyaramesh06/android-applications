package com.example.inclass12;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TripAdapter extends ArrayAdapter<Trip> {
    public TripAdapter(@NonNull Context context, int resource, @NonNull List<Trip> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Trip trip = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.triplistview, parent, false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.tripDate = convertView.findViewById(R.id.date);
            viewHolder.tripName = convertView.findViewById(R.id.name);
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.tripName.setText(trip.getTrip_name());
        viewHolder.tripDate.setText(trip.getDate());;

        return convertView;
    }

    public static class ViewHolder
    {
        TextView tripName, tripDate;
    }
}
