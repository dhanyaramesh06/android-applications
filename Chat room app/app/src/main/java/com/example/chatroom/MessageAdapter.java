package com.example.chatroom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    public static final int WITH_IMAGE = 1;
    public static final int WITHOUT_IMAGE = 0;
    public MessageAdapter(@NonNull Context context, int resource,@NonNull List<Message> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView, @NonNull ViewGroup parent) {
        //pos = position;
        Message msg = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null)
        {
            if(getItemViewType(position) == WITH_IMAGE)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.messagelist,parent,false);
            }
            else if(getItemViewType(position) == WITHOUT_IMAGE)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.messagelist_without_img,parent,false);
            }
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.message = convertView.findViewById(R.id.message);
            viewHolder.fname = convertView.findViewById(R.id.first_name);
            viewHolder.time = convertView.findViewById(R.id.time);
            viewHolder.delete = convertView.findViewById(R.id.delete_btn);
            viewHolder.image = convertView.findViewById(R.id.image);
            viewHolder.delete.setTag(position);
        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.message.setText(msg.message);
        viewHolder.fname.setText(msg.first_name);
        PrettyTime p = new PrettyTime();
        viewHolder.time.setText(p.format(msg.getTime()));
        if(getItemViewType(position) == WITH_IMAGE)
        {
            Picasso.get().load(msg.getImage_url()).into(viewHolder.image);
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "Inside delete");
                int pos = (int) v.getTag();
                Message deleteMsg = getItem(pos);
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Messages").child(deleteMsg.getKey());
                myRef.removeValue();
            }
        });
        return convertView;

    }

    @Override
    public int getItemViewType(int position) {
        Message msg = getItem(position);
        if(msg.getImage_url().equals("No image found"))
            return WITHOUT_IMAGE;
        else
            return WITH_IMAGE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private static class ViewHolder{
        TextView message, fname, time;
        ImageButton delete;
        ImageView image;
    }
}
