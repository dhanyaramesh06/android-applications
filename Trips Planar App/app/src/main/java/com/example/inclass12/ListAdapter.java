package com.example.inclass12;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Place> {
    private final List<Place> list;
    private final Context context;
    AdapterInterface obj;

    public ListAdapter(@NonNull Context context, int resource, @NonNull List<Place> objects, AdapterInterface obj) {
        super(context, resource, objects);
        this.context = context;
        list = objects;
        this.obj = obj;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview,parent,false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.place = convertView.findViewById(R.id.place);
            viewHolder.add = convertView.findViewById(R.id.checkBox);
            viewHolder.add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    list.get(getPosition).setSelected(buttonView.isChecked());
                    obj.onclick(getPosition);
                }
            });

            convertView.setTag(R.id.place,viewHolder.place);
            convertView.setTag(R.id.checkBox,viewHolder.add);
        }
        else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.add.setTag(position);
        viewHolder.place.setText(list.get(position).getName());
        viewHolder.add.setChecked(list.get(position).isSelected());
        return convertView;
    }

    public static class ViewHolder
    {
        TextView place;
        CheckBox add;
    }

    public interface AdapterInterface
    {
        void onclick(int pos);
    }
}
