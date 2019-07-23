package com.example.inclass10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Notes extends AppCompatActivity implements NotesAdapter.DeleteNote {
    TextView greetings;
    Button add_note;
    ImageButton logout;
    ListView notes_list;
    String name, id, token;
    NotesAdapter adapter;
    NotePojo classobj;
    ArrayList<NotePojo> notesList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setTitle("Notes");

        greetings = findViewById(R.id.name);
        add_note = findViewById(R.id.addnote_btn);
        logout = findViewById(R.id.logout);
        notes_list = findViewById(R.id.listview);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Notes.this);
        token = sharedPreferences.getString("token","no token found");
        Log.d("demo","Token from shared preference: "+token);
        adapter = new NotesAdapter(getBaseContext(), R.layout.notes, notesList,Notes.this);
        notes_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getUserDetails();

        getAllNotes();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    logout();
                }catch(Exception e)
                {
                    Log.d("demo","Error in logout click: "+e);
                }
            }
        });

        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notes.this,AddNote.class);
                startActivity(intent);
            }
        });


//        notes_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                Log.d("demo","clicked an item in list view");
//                NotePojo obj = adapter.getItem(position);
//                Log.d("demo","object from on click in list view "+obj);
//                Intent intent = new Intent(Notes.this,DisplayNote.class);
//                intent.putExtra("display", obj);
//                startActivity(intent);
//                    }
//                });
    }

    public void logout()
    {
                Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/logout")
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("demo","Client request failed in logout: "+e);
            }

            @Override
            public void onResponse(Call call, Response response)
            {
                try
                {
                    if(response.isSuccessful())
                    {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Notes.this);
                        sharedPreferences.edit().clear().commit();
                        Intent intent = new Intent(Notes.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Log.d("demo","Unexpected response in logout:" + response);
                    }
                }catch(Exception e)
                {
                    Log.d("demo", "On Response exception in logout "+e);
                }
            }
        });
    }

    public void getUserDetails()
    {

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me")
                .addHeader("x-access-token", token)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("demo","Client request failed in get user details: "+e);
            }

            @Override
            public void onResponse(Call call, Response response)
            {
                try
                {
                    if(response.isSuccessful())
                    {
                        ResponseBody responseBody = response.body();
                        JSONObject object = new JSONObject(responseBody.string());
                        id = object.getString("_id");
                        name = object.getString("name");
                        greetings.setText("Hey "+name+"!!!");
                        Log.d("demo", "id: "+id);
                        Log.d("demo", "name: "+name);
                    }
                    else
                    {
                        Log.d("demo","Unexpected response in get user details:" + response);
                    }

                }catch(Exception e)
                {
                    Log.d("demo", "On Response exception "+e);
                }
            }
        });
    }

    public void getAllNotes()
    {
       Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall")
                .addHeader("x-access-token", token)
                .build();

        //Log.d("demo","Add note request: "+request.toString());

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("demo","Client request failed in get all: "+e);
            }

            @Override
            public void onResponse(Call call, Response response)
            {
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        JSONObject object = new JSONObject(responseBody.string());
                        JSONArray notes = object.getJSONArray("notes");
                        for(int i = 0; i<notes.length(); i++)
                        {
                            JSONObject note_json = notes.getJSONObject(i);
                            classobj = new NotePojo();
                            classobj.note = note_json.getString("text");
                            classobj.note_id = note_json.getString("_id");
                            Log.d("demo","Message note:" + classobj);
                            notesList.add(classobj);
                            adapter = new NotesAdapter(getBaseContext(), R.layout.notes, notesList,Notes.this);
                            notes_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        Log.d("demo","Unexpected response in get all:" + response.body().string());
                    }
                }catch(Exception e)
                {
                    Log.d("demo", "On Response exception in get all "+e);
                }
            }
        });
    }

    @Override
    public void deleteItem(NotePojo delItem) {
        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/delete?msgId="+delItem.note_id)
                .addHeader("x-access-token", token)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("demo","Client request failed in get all: "+e);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        JSONObject object = new JSONObject(responseBody.string());
                        Boolean delete = object.getBoolean("delete");
                        Log.d("demo","On click of delete: "+delete);
                        for(int i = 0 ; i<notesList.size(); i++)
                        {
                            if(notesList.get(i).equals(delItem))
                                notesList.remove(i);
                        }
                        Notes.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    else
                    {
                        Log.d("demo","Unexpected response in delete:" + response);
                    }
                }catch (Exception e)
                {
                    Log.d("demo", "On Response exception "+e);
                }
            }
        });
    }

    @Override
    public void onclick(int position) {
        Log.d("demo","clicked an item in list view");
        NotePojo obj = adapter.getItem(position);
        Log.d("demo","object from on click in list view "+obj);
        Intent intent = new Intent(Notes.this,DisplayNote.class);
        intent.putExtra("display", obj);
        startActivity(intent);
    }



}
