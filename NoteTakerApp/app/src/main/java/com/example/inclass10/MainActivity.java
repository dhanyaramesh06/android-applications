/*
GROUP 4
CHELLA ARCHANA KANDASWAMY - 801085762
DHANYA RAMESH - 801073179
 */

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

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Button login, signup;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        email = findViewById(R.id.email);
        password = findViewById(R.id.pwd);
        login = findViewById(R.id.login_btn);
        signup = findViewById(R.id.signup_btn);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        token = sharedPreferences.getString("token","no token found");
        Log.d("demo","Token from shared preference: "+token);

        if(!token.equals("no token found"))
        {
            Intent intent = new Intent(MainActivity.this,Notes.class);
            startActivity(intent);
        }
        else {

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Signup.class);
                    startActivity(intent);
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            run();
                        } catch (Exception e) {
                            Log.d("demo", "On click of login - " + e);
                        }
                    }
                }
            });
        }
    }


    public void run()
    {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email.getText().toString())
                .add("password", password.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("demo","Client request failed in login: "+e);
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
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("auth",auth);
                            editor.putString("token",token);
                            editor.commit();
                            Intent intent = new Intent(MainActivity.this,Notes.class);
                            startActivity(intent);
                        }
                        else
                        {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                    else
                    {
                        Log.d("demo","Unexpected response in Login page:" + response.body().string());
                    }

                }catch(Exception e)
                {
                    Log.d("demo", "On Response exception in login "+e);
                }
            }
        });
    }
}
