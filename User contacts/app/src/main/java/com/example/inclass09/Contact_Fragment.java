package com.example.inclass09;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class Contact_Fragment extends Fragment {

    ContactInterface object;
    ArrayList<Contact> contacts;
    ContactAdapter adapter;
    public Contact_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            object = (ContactInterface) getActivity();
        }catch (Exception e)
        {
            Log.d("demo","Exception in Contacts Fragment"+e);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list = getActivity().findViewById(R.id.list);

        getActivity().findViewById(R.id.create_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.onCreateContact();
            }
        });

        getActivity().findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.onSignOut();
            }
        });
        Bundle b =this.getArguments();
        if(b!=null){
            Log.d("demo","Contact_Fragment");
            contacts = (ArrayList) b.getSerializable("contacts");
            adapter = new ContactAdapter(getContext(),R.layout.listview,contacts);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contact c = contacts.get(position);
                contacts.remove(position);
                adapter.notifyDataSetChanged();
                object.onLongClick(c);
                return false;
            }
        });
    }

    public interface ContactInterface{
        void onCreateContact();
        void onSignOut();
       void onLongClick(Contact c);
    }
}
