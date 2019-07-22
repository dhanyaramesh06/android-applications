package com.example.inclass2a;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.floor;

public class MainActivity extends AppCompatActivity {
    private double weigh,bmi;
    private int height_ft,height_inches,final_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText weight = findViewById(R.id.weight);
        final EditText h_feet = findViewById(R.id.height_feet);
        final EditText h_inches = findViewById(R.id.height_inches);
        final TextView bmi_res = findViewById(R.id.bmi_result);
        final TextView bmi_stat = findViewById(R.id.bmi_status);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if((weight.getText().toString()).equals(""))
            {
                weight.setError("Enter valid weight");
            } else if((h_feet.getText().toString()).equals("")){
                h_feet.setError("Enter valid height in feet");
            }else if((h_inches.getText().toString()).equals(""))
            {
                h_inches.setError("Enter valid height in inches");
            }
            try
            {
                weigh= Double.parseDouble(weight.getText().toString());
                height_ft=Integer.parseInt(h_feet.getText().toString());
                height_inches = Integer.parseInt(h_inches.getText().toString());
                if(weigh<=0 || height_ft<=0 || height_inches<0)
                {
                    Toast.makeText(getApplicationContext(), "Enter a valid non negative and non-zero value", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final_height = (height_ft * 12) + height_inches;
                    bmi = floor(((weigh) / (final_height * final_height)) * 703);
                    bmi_res.setText("Your BMI:" + bmi);
                    if (bmi <= 18.5) {
                        bmi_stat.setText("You are Underweight");
                    } else if (bmi >= 18.5 && bmi <= 24.9) {
                        bmi_stat.setText("You are Normal Weight");
                    } else if (bmi >= 25 && bmi <= 29.9) {
                        bmi_stat.setText("You are Overweight");
                    } else if (bmi >= 30.0) {
                        bmi_stat.setText("You are Obese");
                    }
                    Toast.makeText(getApplicationContext(), "BMI Calculated", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e)
            {
                Log.d("Error","Enter a valid input");
            }

            }
        });

    }


}
