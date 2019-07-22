package com.example.inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        setTitle("Display My Profile");

        TextView tv = findViewById(R.id.textView3);
        TextView tv1 = findViewById(R.id.textView4);
        TextView tv2 = findViewById(R.id.textView6);
        ImageView img = findViewById(R.id.imageView3);
        Button b = findViewById(R.id.button);
        Log.d("demo","third");
        try {
            if (getIntent() != null && getIntent().getExtras() != null) {
                Student student = (Student) getIntent().getExtras().getSerializable("info");
                tv.setText(student.name);
                tv1.setText(String.valueOf(student.student_id));
                tv2.setText(student.dept);
                if(student.image_id!=0)
                {
                    img.setImageResource(student.image_id);
                }
                else{
                    img.setImageResource(R.drawable.person);
                }


            }
        }
        catch(Exception e)
        {
            Log.d("demo",e+"");
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
