package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class GeneratedPasswords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);
        setTitle("Generated Passwords");
        ScrollView thread_scroll = findViewById(R.id.thread_scroll);
        ScrollView async_scroll = findViewById(R.id.async_scroll);
        Button finish = findViewById(R.id.finish);

        LinearLayout thread_layout = findViewById(R.id.thread_layout);
        LinearLayout async_layout = findViewById(R.id.async_layout);

        LayoutInflater inflater = LayoutInflater.from(this);

        if(getIntent() != null && getIntent().getExtras() != null)
        {
            Log.d("demo", "In Generated Passwords class");
            ArrayList<String> thread = getIntent().getStringArrayListExtra("thread");
            Log.d("demo", ""+thread);
            ArrayList<String> async = getIntent().getStringArrayListExtra("async");
            Log.d("demo",""+async);


            for(String s:thread)
            {
                View view1 = inflater.inflate(R.layout.passwords,thread_layout, false);
                TextView tv_pwd = (TextView) view1.findViewById(R.id.tv_pwd);
                tv_pwd.setText(s);
                if(tv_pwd.getParent() != null) {
                    ((ViewGroup)tv_pwd.getParent()).removeView(tv_pwd); // <- fix
                }
                thread_layout.addView(view1);
                Log.d("demo",""+s);
            }


            for(String s:async)
            {
                View view2 = inflater.inflate(R.layout.passwords,thread_layout, false);
                TextView tv_pwd = (TextView) view2.findViewById(R.id.tv_pwd);
                tv_pwd.setText(s);
                if(tv_pwd.getParent() != null) {
                    ((ViewGroup)tv_pwd.getParent()).removeView(tv_pwd); // <- fix
                }
                async_layout.addView(view2);
                Log.d("demo",""+s);
            }


        }
        else
            Log.d("demo", "Error in intents");

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
