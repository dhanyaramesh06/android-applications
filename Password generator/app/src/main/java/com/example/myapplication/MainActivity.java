/*
Group 4 - HW2
Chella Archana Kandaswamy - 801085762
Dhanya Ramesh - 801073179
 */

package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
boolean flag = false;
boolean thread = false;
boolean async = false;
int tcount, acount, tlength, alength;

ProgressDialog progressDialog;
SeekBar thread_pwdcount, async_pwdcount, thread_pwdlength, async_pwdlength;
TextView thread_count, thread_length, async_count, async_length;
Button generate;

ArrayList<String> threadpwd = new ArrayList<>();
ArrayList<String> asyncpwd = new ArrayList<>();

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.d("demo", "In handler");
            switch(msg.what)
            {
                case 100: {
                    Log.d("demo", "In case 100");
                    progressDialog.setProgress(progressDialog.getProgress() + msg.getData().getInt("progress"));
                    break;
                }
                case 200: {
                    Log.d("demo","In case 200");
                    threadpwd = msg.getData().getStringArrayList("pwd");
                    thread = true;
                    flag = false;
                    Log.d("demo", "Thread " + threadpwd);
                    executeIntent();
                    break;
                }
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Password Generator");
        thread_pwdcount = findViewById(R.id.thread_pwdcount);
        thread_pwdlength = findViewById(R.id.thread_pwdlength);
        async_pwdcount = findViewById(R.id.async_pwdcount);
        async_pwdlength = findViewById(R.id.async_pwdlength);
        thread_count = findViewById(R.id.thread_count);
        thread_length = findViewById(R.id.thread_length);
        async_count = findViewById(R.id.async_count);
        async_length = findViewById(R.id.async_length);
        generate = findViewById(R.id.gbutton);

        //Initialization
        thread_pwdcount.setMax(10);
        thread_pwdcount.setProgress(1);
        thread_count.setText("1");
        async_pwdcount.setMax(10);
        async_pwdcount.setProgress(1);
        async_count.setText("1");
        thread_pwdlength.setMax(23);
        thread_pwdlength.setProgress(7);
        thread_length.setText("7");
        async_pwdlength.setMax(23);
        async_pwdlength.setProgress(7);
        async_length.setText("7");

        thread_pwdcount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                thread_count.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        thread_pwdlength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                thread_length.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        async_pwdcount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                async_count.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        async_pwdlength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                async_length.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcount = Integer.parseInt(thread_count.getText().toString());
                acount = Integer.parseInt(async_count.getText().toString());
                tlength = Integer.parseInt(thread_length.getText().toString());
                alength = Integer.parseInt(async_length.getText().toString());
                if(flag == false)
                {
                    if(tcount <1 || acount <1) {
                        Toast.makeText(MainActivity.this, "Minimum password count should be 1", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                    if(tlength <7 || alength < 7) {
                        Toast.makeText(MainActivity.this, "Minimum password length should be 7", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                }

                if(flag != true)
                {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMax(acount+tcount);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMessage("Generating Passwords");
                    progressDialog.setCancelable(false);

                    ExecutorService threadPool = Executors.newFixedThreadPool(2);
                    threadPool.execute(new ThreadPwd(tlength));
                    new AsyncPwd().execute(alength);
                    Log.d("demo",""+thread+""+async);
                }
                flag = false;
            }
        });
    }

    public void executeIntent()
    {
        if(thread == true && async == true)
        {
            progressDialog.dismiss();
            Intent intent = new Intent(getBaseContext(),GeneratedPasswords.class);
            intent.putExtra("thread",threadpwd);
            intent.putExtra("async",asyncpwd);
            startActivity(intent);
            thread = false;
            async = false;
            threadpwd.clear();
            asyncpwd.clear();
        }
        else
            Log.d("demo","Error "+thread+""+async);

    }

    public class ThreadPwd implements Runnable
    {
        int tlength;
        public ThreadPwd(int tlength) {
            this.tlength = tlength;
        }

        @Override
        public void run()
        {
            ArrayList<String> threadpwd = new ArrayList<>();
            for(int count = 1; count <= tcount; count++)
            {
                String pwd = Util.getPassword(tlength);
                threadpwd.add(pwd);
                Message msgcount = new Message();
                Bundle b1 = new Bundle();
                msgcount.what = 100;
                b1.putInt("progress",count);
                msgcount.setData(b1);
                Log.d("demo","Thread result"+count);
                handler.sendMessage(msgcount);
            }
            Log.d("demo","Thread "+threadpwd);
            Message msg = new Message();
            Bundle b2 = new Bundle();
            msg.what = 200;
            b2.putStringArrayList("pwd",threadpwd);
            msg.setData(b2);
            handler.sendMessage(msg);
        }
    }

    public class AsyncPwd extends AsyncTask<Integer, Integer, ArrayList<String>>
    {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            asyncpwd = s;
            flag = false;
            async = true;
            Log.d("demo","AsyncTask "+asyncpwd);
            executeIntent();
        }

        @Override
        protected ArrayList<String> doInBackground(Integer... integers) {
            ArrayList<String> asyncpwd = new ArrayList<>();
            for(int count = 1 ; count <= acount; count++)
            {
                String pwd = Util.getPassword(integers[0]);
                asyncpwd.add(pwd);
                publishProgress(count);
                Log.d("demo","Async result"+count);
            }
            Log.d("demo","AsyncTask "+asyncpwd);
            return asyncpwd;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(progressDialog.getProgress()+values[0]);
        }
    }
}
