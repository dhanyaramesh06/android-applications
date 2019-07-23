package com.example.inclass10;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<NotePojo>{

    DeleteNote obj;

    public NotesAdapter(@NonNull Context context, int resource, @NonNull List<NotePojo> objects,DeleteNote obj) {
        super(context, resource, objects);
        this.obj = obj;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       NotePojo note = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notes,parent,false);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.msg = convertView.findViewById(R.id.textView);
            viewHolder.delete = convertView.findViewById(R.id.delete_btn);
            viewHolder.delete.setTag(position);
            viewHolder.msg.setTag(position);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.msg.setText(note.note);
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                NotePojo delItem = getItem(pos);
                obj.deleteItem(delItem);
            }
        });
        viewHolder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();

                obj.onclick(pos);
            }
        });


        return convertView;
    }

    public static class ViewHolder{
        TextView msg;
        ImageButton delete;
    }

    public interface DeleteNote{
        void deleteItem(NotePojo delItem);
        void onclick(int pos);
    }
}
