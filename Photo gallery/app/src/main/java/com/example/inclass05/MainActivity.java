package com.example.inclass05;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetImageAsync.SetUIComp {

    String[] search,imgUrl;
    int imgcount = 0;
    ProgressBar progressBar;
    TextView load;
    ImageView image;
    ImageButton previous, next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Main Activity");

        final TextView keyword = findViewById(R.id.textView);
        image = findViewById(R.id.imageView);
        previous = findViewById(R.id.imageButton);
        next = findViewById(R.id.imageButton2);
        progressBar = findViewById(R.id.progressBar);
        load = findViewById(R.id.textView2);
        load.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);
        previous.setEnabled(false);
        next.setEnabled(false);
        Button go = findViewById(R.id.button);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
            new GetKeywordAsync().execute(" http://dev.theappsdr.com/apis/photos/keywords.php");
            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose a Keyword").setItems(search, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            String s = search[which];
                            keyword.setText(s);
                            RequestParams params = new RequestParams();
                            params.addParameter("keyword",search[which]);
                            new GetImageUrlAsync(params).execute("http://dev.theappsdr.com/apis/photos/index.php");
                        }
                    });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imgcount < imgUrl.length -1)
                    {
                        imgcount = imgcount+1;
                        new GetImageAsync(MainActivity.this).execute(imgUrl[imgcount]);
                    }
                    else
                    {
                        imgcount = 0;
                        new GetImageAsync(MainActivity.this).execute(imgUrl[imgcount]);
                    }
                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imgcount != 0) {
                        imgcount = imgcount - 1;
                        new GetImageAsync(MainActivity.this).execute(imgUrl[imgcount]);
                    }
                    else {
                        imgcount = imgUrl.length - 1;
                        new GetImageAsync(MainActivity.this).execute(imgUrl[imgcount]);
                    }
                }
            });
        }
    }

    @Override
    public void setUICompStatus(Bitmap bitmap) {
        image.setVisibility(View.VISIBLE);
        image.setImageBitmap(bitmap);
        progressBar.setVisibility(View.INVISIBLE);
        load.setVisibility(View.INVISIBLE);
        if(imgUrl.length > 1)
        {
            previous.setEnabled(true);
            next.setEnabled(true);
        }
    }

    @Override
    public void setLoading() {
        image.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
    }

    public class GetKeywordAsync extends AsyncTask<String,Void, String>
    {

        @Override
        protected void onPostExecute(String strings) {
            if(strings != null)
            {
                search = strings.split(";");
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;

    }
    }

    public class GetImageUrlAsync extends AsyncTask<String,Void,String>
    {
        RequestParams mparams;
        public GetImageUrlAsync(RequestParams params)
        {
            mparams=params;
        }

        @Override
        protected void onPostExecute(String strings) {
            Log.d("demo","post execute"+strings);
            if(strings.isEmpty())
            {
                Toast.makeText(getBaseContext(), "No Images Found", Toast.LENGTH_SHORT).show();
                Log.d("demo","Image view invisible");
                image.setVisibility(View.INVISIBLE);
                next.setEnabled(false);
                previous.setEnabled(false);
            }
            else
            {
                imgUrl = strings.split(".jpg");
                for (int i = 0; i < imgUrl.length; i++)
                {
                        imgUrl[i] = imgUrl[i] + ".jpg";
                }

                new GetImageAsync(MainActivity.this).execute(imgUrl[imgcount]);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            try {
                URL url = new URL(mparams.getEncodedUrl(strings[0]));
                Log.d("demo",url+"URL in async");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
                }
                Log.d("demo",result+"Result");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }
    }
}
