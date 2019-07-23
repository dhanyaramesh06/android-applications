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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText first_name, last_name, email, password, repeat_password;
    Button cancel, signup;
    FirebaseAuth mauth;
    FirebaseUser user;
    DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");

        mauth = FirebaseAuth.getInstance();
        myref = FirebaseDatabase.getInstance().getReference("Messages");

        first_name = findViewById(R.id.fname);
        last_name = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pwd);
        repeat_password = findViewById(R.id.confirm_pwd);
        cancel = findViewById(R.id.cancel_btn);
        signup = findViewById(R.id.signup_btn);


        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (first_name.getText().toString().isEmpty() || last_name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || repeat_password.getText().toString().isEmpty())
                {
                    Toast.makeText(Signup.this, "Fill in the details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (password.getText().toString().equals(repeat_password.getText().toString()))
                    {
                        mauth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    user = mauth.getCurrentUser();
                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(first_name.getText().toString() + " " + last_name.getText().toString()).build();
                                    user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Intent intent = new Intent(Signup.this, ChatRoom.class);
                                                intent.putExtra("firstname",first_name.getText().toString());
                                                intent.putExtra("lastname",last_name.getText().toString());
                                                Toast.makeText(getBaseContext() ,"User Created", Toast.LENGTH_SHORT).show();
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(getBaseContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else
                        Toast.makeText(getBaseContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
