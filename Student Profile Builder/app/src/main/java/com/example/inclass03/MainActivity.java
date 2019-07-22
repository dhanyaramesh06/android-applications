package com.example.inclass03;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView img;

    final static int REQ_CODE=100;
    private int res,flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Profile");

        final EditText fname = findViewById(R.id.fname);
        final EditText lname = findViewById(R.id.lname);
        final EditText studId = findViewById(R.id.stud_id);
        final RadioButton dept1 = findViewById(R.id.dept1);
        final RadioButton dept2 = findViewById(R.id.dept2);
        final RadioButton dept3 = findViewById(R.id.dept3);
        final RadioButton dept4 = findViewById(R.id.dept4);
        Button btn = findViewById(R.id.button_label);


        img = findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),SecondActivity.class);
                startActivityForResult(intent,REQ_CODE);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String dept="" ;
                try {
                    if(flag == 0) {
                        if (fname.getText().toString().equals("")) {
                            fname.setError("Enter a valid First Name");
                            flag=1;
                        }
                        if (lname.getText().toString().equals("")) {
                            lname.setError("Enter a valid Last Name");
                            flag=1;
                        }
                        if (studId.getText().toString().equals("") || studId.getText().toString().length() < 9) {
                            studId.setError("Enter a valid 9 digit Student Id");
                            flag=1;
                        }
                        if(res==0)
                        {
                            flag=1;
                            Toast.makeText(MainActivity.this, "Select an avatar", Toast.LENGTH_SHORT).show();
                        }

                        if (dept1.isChecked()) {
                            dept = "CS";
                            Log.d("demo", dept);
                        } else if (dept2.isChecked()) {
                            dept = "SIS";
                        } else if (dept3.isChecked()) {
                            dept = "BIO";
                        } else if (dept4.isChecked()) {
                            dept = "Other";
                        } else {
                            flag=1;
                            throw new Exception();
                        }
                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Please select a valid department", Toast.LENGTH_SHORT).show();
                }
                    if(flag!=1) {
                        String first_name = fname.getText().toString();
                        String last_name = lname.getText().toString();
                        int stud_id = Integer.parseInt(studId.getText().toString());
                        String name = first_name + " " + last_name;
                        int image_id = res;

                        Intent i = new Intent(MainActivity.this, ThirdActivity.class);
                        Student student = new Student(name, stud_id, dept, image_id);
                        i.putExtra("info", student);
                        Log.d("demo", i + "");
                        startActivity(i);
                    }
                    flag = 0;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(REQ_CODE==100)
        {
            if(resultCode==RESULT_OK && data.getExtras()!=null)
            {
                Bundle b = data.getExtras();
                res = b.getInt("image");
                Log.d("demo", b + "");
                img.setImageResource(res);
            }
        }
    }
}
