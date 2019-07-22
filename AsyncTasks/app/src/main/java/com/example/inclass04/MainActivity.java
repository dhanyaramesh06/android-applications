/* Assignment Inclass04:
Group 4:
Chella Archana Kandaswamy - 801085762
Dhanya Ramesh-801073179
*/
package com.example.inclass04;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity{

    ProgressBar progressBar;
    ImageView image;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.d("demo",msg.what+"");
            progressBar.setProgress(msg.what);
            progressBar.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap((Bitmap) msg.obj);

            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Display Image");

        image = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        Button btn = findViewById(R.id.button);
        progressBar.setVisibility(View.INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new sendImageAsync().execute("https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg");
            }
        });

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                ExecutorService threadPool = Executors.newFixedThreadPool(3);
                threadPool.execute(new getImageThread());
            }
        });
    }
    public class getImageThread implements Runnable{

        @Override
        public void run() {
            Bitmap myBitmap=null;
            Message msg =new Message();

            try {
                URL url = new URL("https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                }
                else{
                    Log.d("demo","error");
                }

                if(myBitmap!=null)
                {
                    for (int i = 0; i < 100; i++) {
                        for (int j = 0; j < 100000; j++) {

                        }
                         msg.what=i;
                    }
                    msg.obj=myBitmap;
                    handler.sendMessage(msg);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class sendImageAsync extends AsyncTask<String,Integer,Bitmap>
    {

        @Override
        protected void onPreExecute() {
            image.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            progressBar.setMax(100);
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(s);
            progressBar.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap img= getImageBitmap(strings);
            return img;
        }
        Bitmap getImageBitmap(String... strings) {
            Bitmap myBitmap=null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                Log.d("demo",connection.getResponseCode()+"");
                Log.d("demo","In try");
                for (int i = 0; i < 100; i++) {
                    for (int j = 0; j < 100000; j++) {

                    }
                    publishProgress(i);
                }
                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                }
                else{
                    Log.d("demo","error");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return myBitmap;

        }


    }
}
