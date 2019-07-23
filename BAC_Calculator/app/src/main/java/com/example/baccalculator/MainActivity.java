/*
Homework 1
Chella Archana Kandaswamy (801085762)
Dhanya Ramesh (801073179)
*/

package com.example.baccalculator;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    double wt = 0.0, bac = 0.0, bac_temp = 0.0;
    boolean clicked;
    int clickcount = 0;
    double a;
    ArrayList<Double> drinks_consumed = new ArrayList<Double>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));*/
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);

        final EditText weight = findViewById(R.id.etweight);
        final Switch gender = findViewById(R.id.genderSwitch);
        final Button save = findViewById(R.id.btSave);
        final RadioButton size1 = findViewById(R.id.size1);
        final RadioButton size2 = findViewById(R.id.size2);
        final RadioButton size3 = findViewById(R.id.size3);
        final SeekBar seek_bar = findViewById(R.id.seekBar);
        final TextView seek_value = findViewById(R.id.tvseek);
        final Button add_drink = findViewById(R.id.btadd_drink);
        final Button reset = findViewById(R.id.btreset);
        final TextView bac_result = findViewById(R.id.tvbaclevel);
        final TextView bac_status = findViewById(R.id.tvresult);
        final ProgressBar prog_bar = findViewById(R.id.progressBar);

            seek_bar.setProgress(5);
            seek_bar.setMax(25);
            prog_bar.setMax(25);
            prog_bar.setProgress(0);
            size1.setChecked(true);
            seek_value.setText("5");
            bac_result.setText("0.00");
            bac_status.setBackgroundColor(Color.parseColor("#006400"));
            bac_status.setTextColor(Color.WHITE);
            bac_status.setText("You're Safe");

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //double wt = Double.parseDouble(weight.getText().toString());
                    clicked = true;
                    clickcount = clickcount + 1;
                    if(clickcount == 1)
                    {
                        checkWeight(weight);
                    }
                    else
                    {
                        checkWeight(weight);
                        recalculate(drinks_consumed,weight,gender,save,add_drink,prog_bar,bac_result,bac_status);
                    }
                }
            });

            gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        gender.setText("Male");
                    else
                        gender.setText("Female");

                }
            });

            seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progress = (progress/5)*5;
                    seek_value.setText(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            add_drink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clicked == true)
                        calculate(weight,gender,size1,size2,size3,seek_value,save,add_drink,prog_bar,bac_result,bac_status);
                    else
                        Toast.makeText(MainActivity.this, "Save the weight & gender to continue!", Toast.LENGTH_SHORT).show();

                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    weight.setText("");
                    weight.setError(null);
                    if(gender.isChecked())
                        gender.toggle();
                    size1.setChecked(true);
                    seek_bar.setProgress(5);
                    seek_value.setText("5");
                    save.setEnabled(true);
                    reset.setEnabled(true);
                    add_drink.setEnabled(true);
                    prog_bar.setProgress(0);
                    bac = 0.0;
                    bac_temp = 0.0;
                    clicked = false;
                    clickcount = 0;
                    drinks_consumed.clear();
                    bac_result.setText("0.00");
                    bac_status.setBackgroundColor(Color.parseColor("#006400"));
                    bac_status.setTextColor(Color.WHITE);
                    bac_status.setText("You're Safe");
                }
            });
    }

    public void checkWeight(EditText weight)
    {
        if (weight.getText().toString().isEmpty() || Double.parseDouble(weight.getText().toString()) < 1)
            weight.setError("Enter the weight in lb");
    }
    public void calculate(EditText weight, Switch gender,RadioButton size1,RadioButton size2, RadioButton size3, TextView seek_value, Button save, Button add_drink,ProgressBar prog_bar, TextView bac_result, TextView bac_status)
    {
                wt = Double.parseDouble(weight.getText().toString());
                int seek_val = Integer.parseInt(seek_value.getText().toString());
                if (size1.isChecked())
                {
                    a=1*seek_val;
                    if (gender.isChecked())
                        bac = ((a * 6.24) / (wt * 0.68)) / 100;
                    else
                        bac = ((a * 6.24) / (wt * 0.55)) / 100;

                }
                if (size2.isChecked())
                {
                    a=5*seek_val;
                    if (gender.isChecked())
                        bac = ((a * 6.24) / (wt * 0.68)) / 100;
                    else
                        bac = ((a * 6.24) / (wt * 0.55)) / 100;
                }
                if (size3.isChecked())
                {
                    a=12*seek_val;
                    if (gender.isChecked())
                        bac = ((a * 6.24) / (wt * 0.68)) / 100;
                    else
                        bac = ((a * 6.24) / (wt * 0.55)) / 100;
                }
                //Log.d("demo", bac + "");
                //Toast.makeText(MainActivity.this, bac + "", Toast.LENGTH_SHORT).show();
                drinks_consumed.add(a);
                bac = bac + bac_temp;
                bac_temp = bac;
                String bac_round = String.format("%.2f", bac);
                bac_result.setText(String.valueOf(bac_round));
                int pg_val = (int) (Math.round(bac * 100));
                prog_bar.setProgress(pg_val);

                if (Math.round(pg_val) >= 25) {
                    Toast.makeText(MainActivity.this, "No more drinks for you", Toast.LENGTH_SHORT).show();
                    save.setEnabled(false);
                    add_drink.setEnabled(false);
                }

                if (bac <= 0.08) {
                    bac_status.setBackgroundColor(Color.parseColor("#006400"));
                    bac_status.setTextColor(Color.WHITE);
                    bac_status.setText("You're Safe");
                } else if (bac > 0.08 && bac < 0.20) {
                    bac_status.setBackgroundColor(Color.parseColor("#FFA500"));
                    bac_status.setTextColor(Color.WHITE);
                    bac_status.setText("Be careful...");
                } else if (bac >= 0.20) {
                    bac_status.setBackgroundColor(Color.parseColor("#8B0000"));
                    bac_status.setTextColor(Color.WHITE);
                    bac_status.setText("Over the Limit!");
                }

        }
        public void recalculate(ArrayList drinks_consumed, EditText weight, Switch gender,Button save, Button add_drink,ProgressBar prog_bar, TextView bac_result, TextView bac_status )
        {
            bac = 0.0;
            bac_temp = 0.0;
            wt = Double.parseDouble(weight.getText().toString());
            for(int i = 0; i<drinks_consumed.size();i++ )
            {
                if(gender.isChecked()){
                    bac= (((double)drinks_consumed.get(i)*6.24)/(wt*0.68))/100;
                   // Log.d("demo", "" + bac);
                    }
                else {
                   // Log.d("demo", "female");
                    bac = (((double) drinks_consumed.get(i) * 6.24) / (wt * 0.55)) / 100;
                    //Log.d("demo", "" + bac);
                }
                //Log.d("demo",""+drinks_consumed.get(i));
                //Log.d("demo",""+wt*0.55);
                bac = bac + bac_temp;
                bac_temp = bac;
            }


            String bac_round = String.format("%.2f", bac);
            bac_result.setText(String.valueOf(bac_round));
            int pg_val = (int) (Math.round(bac * 100));
            prog_bar.setProgress(pg_val);

            if (Math.round(pg_val) >= 25) {
                Toast.makeText(MainActivity.this, "No more drinks for you", Toast.LENGTH_SHORT).show();
                save.setEnabled(false);
                add_drink.setEnabled(false);
            }

            if (bac <= 0.08) {
                bac_status.setBackgroundColor(Color.parseColor("#006400"));
                bac_status.setTextColor(Color.WHITE);
                bac_status.setText("You're Safe");
            } else if (bac > 0.08 && bac < 0.20) {
                bac_status.setBackgroundColor(Color.parseColor("#FFA500"));
                bac_status.setTextColor(Color.WHITE);
                bac_status.setText("Be careful...");
            } else if (bac >= 0.20) {
                bac_status.setBackgroundColor(Color.parseColor("#8B0000"));
                bac_status.setTextColor(Color.WHITE);
                bac_status.setText("Over the Limit!");
            }
        }
    }

