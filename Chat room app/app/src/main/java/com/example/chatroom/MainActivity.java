/*
GROUP 4 : HOMEWORK 06
CHELLA ARCHANA KANDASWAMY - 801085762
DHANYA RAMESH - 801073179
 */

package com.example.chatroom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    Button login,signup;
    FirebaseAuth mauth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        mauth = FirebaseAuth.getInstance();
        user = mauth.getCurrentUser();

        email = findViewById(R.id.email);
        password = findViewById(R.id.pwd);
        login = findViewById(R.id.login_btn);
        signup = findViewById(R.id.signup_btn);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Toast.makeText(this, user.getDisplayName()+" already logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ChatRoom.class);
            Log.d("demo","user name in login; "+user.getDisplayName());
            intent.putExtra("username",user.getDisplayName());
            startActivity(intent);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                {
                    Toast.makeText(getBaseContext(), "Enter valid credentials", Toast.LENGTH_SHORT).show();
                }

                else {
                    mauth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Log.d("demo", "Login Successful");
                                user = mauth.getInstance().getCurrentUser();
                                if (user != null)
                                {
                                    Log.d("demo","user name in login; "+user.getDisplayName());
                                    Intent intent = new Intent(MainActivity.this, ChatRoom.class);
                                    intent.putExtra("username",user.getDisplayName());
                                    startActivity(intent);

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                    Log.d("demo", "Login UnSuccessful");
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                Log.d("demo", "Login UnSuccessful");
                            }
                        }
                        });
                    }
                }

        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Signup.class);
                startActivity(intent);
            }
        });
    }
}
