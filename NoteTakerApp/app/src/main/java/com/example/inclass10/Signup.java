package com.example.inclass10;

import android.content.Context;
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
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;

public class Signup extends AppCompatActivity {
    EditText fname, lname, email, pwd, confirm_pwd;
    Button signup, cancel;
    //Context context = getBaseContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");


        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.pwd);
        confirm_pwd = findViewById(R.id.confirm_pwd);
        signup = findViewById(R.id.signup_btn);
        cancel = findViewById(R.id.cancel_btn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this,MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname.getText().toString().isEmpty() || lname.getText().toString().isEmpty() || email.getText().toString().isEmpty() || pwd.getText().toString().isEmpty() || confirm_pwd.getText().toString().isEmpty() )
                {
                    Toast.makeText(Signup.this, "Fill in the details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(pwd.getText().toString().equals(confirm_pwd.getText().toString()))
                    {
                        try {
                            run();
                        } catch (Exception e) {
                            Log.d("demo","ON CLICK"+e);
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }

    public void run()
    {
        RequestBody formBody = new FormBody.Builder()
                .add("name", fname.getText().toString() +" "+ lname.getText().toString())
                .add("email", email.getText().toString())
                .add("password", pwd.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/register")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("demo","Client request failed: "+e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response){
                try
                {
                    if(response.isSuccessful())
                    {
                        ResponseBody responseBody = response.body();
                        JSONObject object = new JSONObject(responseBody.string());
                        Boolean auth = object.getBoolean("auth");
                        String token = object.getString("token");
                        Log.d("demo", "Auth: "+auth);
                        Log.d("demo", "Token: "+token);
                        if(auth)
                        {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Signup.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("auth",auth);
                            editor.putString("token",token);
                            editor.commit();
                            Signup.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Signup.this, "User created", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(Signup.this,Notes.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Signup.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Signup.this, "User not created", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                    else
                    {
                        Log.d("demo","Unexpected response in signup page:" + response);
                    }

                }catch(Exception e)
                {
                    Log.d("demo", "On Response exception "+e);
                }
            }
        });
    }
}
