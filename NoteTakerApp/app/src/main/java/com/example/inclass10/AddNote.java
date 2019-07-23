package com.example.inclass10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AddNote extends AppCompatActivity {
    EditText note;
    Button post;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setTitle("Add Note");

        note = findViewById(R.id.note);
        post = findViewById(R.id.post_btn);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddNote.this);
        token = sharedPreferences.getString("token","no token found");
        Log.d("demo","Token from shared preference: "+token);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });

    }

    public void post()
    {
        RequestBody formBody = new FormBody.Builder()
                .add("text",note.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/post")
                .addHeader("x-access-token", token)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("demo","Client request failed in post note click: "+e);
            }

            @Override
            public void onResponse(Call call, Response response)
            {
                try {
                    if (response.isSuccessful())
                    {
                        ResponseBody responseBody = response.body();
                        JSONObject object = new JSONObject(responseBody.string());
                        Boolean posted = object.getBoolean("posted");
                        Log.d("demo", "Posted: "+posted);
                        Intent intent = new Intent(AddNote.this, Notes.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Log.d("demo","Unexpected response in post note click:" + response.body().string());
                    }
                }catch(Exception e)
                {
                    Log.d("demo", "On Response exception "+e);
                }
            }
        });
    }
}
