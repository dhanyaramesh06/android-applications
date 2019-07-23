package com.example.inclass09;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    String phone, final_phone;

    public ContactAdapter(@NonNull Context context, int resource,@NonNull List<Contact> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent) {
        Contact contact = getItem(position);
        ViewHolder viewHolder;
        if(convertView ==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.phone);
            viewHolder.tv_email = (TextView) convertView.findViewById(R.id.email);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(contact.name);
        phone = contact.phone;
        final_phone = phone.substring(0,3)+ "-" + phone.substring(3,6)+ "-" +phone.substring(6);
        viewHolder.tv_phone.setText(final_phone);
        viewHolder.tv_email.setText(contact.email);
        if(contact.getImage()!=null)
        {
            Picasso.get().load(contact.getImage()).into(viewHolder.image);
        }
        else
        {
            viewHolder.image.setImageResource(R.drawable.default_photo);
        }
        return  convertView;

    }

    private static class ViewHolder{
        TextView tv_name,tv_phone,tv_email;
        ImageView image;
    }
}
