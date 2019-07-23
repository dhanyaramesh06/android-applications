/*
InClass 06
Dhanya Ramesh - 801073179
 */
package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetNewsAsync.SetUIComp {

    TextView loading,description,news_title,published_at,news_count,total_news,outof;
    ProgressBar progressBar;
    ImageView img;
    ImageButton prev, next;
    int count = 0;
    ArrayList<Articles> articleList = new ArrayList<>();
    String[] category = new String[]{"Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");
        final TextView categ = findViewById(R.id.tv_category);
        final Button go = findViewById(R.id.goButton);

        news_title = findViewById(R.id.tv_news_title);
        published_at = findViewById(R.id.tv_publishedat);
        img = findViewById(R.id.img);
        next = findViewById(R.id.nextButton);
        prev = findViewById(R.id.prevButton);
        description = findViewById(R.id.news_desc);
        loading = findViewById(R.id.tv_loading);
        progressBar = findViewById(R.id.progressBar);
        news_count = findViewById(R.id.tv_inc);
        total_news = findViewById(R.id.tv_total);
        outof = findViewById(R.id.tv_outof);

        news_title.setVisibility(View.INVISIBLE);
        published_at.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        news_count.setVisibility(View.INVISIBLE);
        total_news.setVisibility(View.INVISIBLE);
        outof.setVisibility(View.INVISIBLE);
        next.setEnabled(false);
        prev.setEnabled(false);

        if (isConnected())
        {
            Toast.makeText(MainActivity.this, "Internet connected", Toast.LENGTH_SHORT).show();
            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose Category").setItems(category, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String s = category[which];
                            categ.setText(s);
                            RequestParams params = new RequestParams();
                            params.addParameter("apiKey", "18d8728f08dc4ec4ba86de439f5d2711");
                            params.addParameter("category", category[which]);
                            params.addParameter("country", "us");
                            count = 0;
                            new GetNewsAsync(params, MainActivity.this).execute("https://newsapi.org/v2/top-headlines");
                        }
                    });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Log.d("demo","In Next");
                    if(count < articleList.size()-1)
                    {
                        count = count + 1;
                        setNews();
                    }
                    else
                    {
                        count = 0;
                        setNews();
                    }
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(count != 0)
                    {
                        count = count - 1;
                        setNews();
                    }
                    else
                    {
                        count = articleList.size() - 1;
                        setNews();
                    }
                }
            });
        }
        else
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE))
        {
            return false;
        }
        return true;
    }

    @Override
    public void setUIpreexecute()
    {
        loading.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUIpostexecute(ArrayList<Articles> result)
    {
        if(result != null)
        {
            Log.d("demo","Result "+result);
            articleList = result;
            Articles data = articleList.get(count);
            news_title.setText(data.title);
            published_at.setText(data.publishedAt);
            Picasso.get().load(data.urlToimage).into(img);
            description.setText(data.description);
            loading.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            news_title.setVisibility(View.VISIBLE);
            published_at.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            news_count.setVisibility(View.VISIBLE);
            total_news.setVisibility(View.VISIBLE);
            outof.setVisibility(View.VISIBLE);
            news_count.setText(String.valueOf(count+1));
            total_news.setText(String.valueOf(articleList.size()));
            if(articleList.size() != 1)
            {
                next.setEnabled(true);
                prev.setEnabled(true);
            }
            else
            {
                next.setEnabled(false);
                prev.setEnabled(false);
            }

        }
        else
        {
            Toast.makeText(this, "No News Found", Toast.LENGTH_SHORT).show();
        }

    }

    public void setNews()
    {
        Articles data = articleList.get(count);
        news_title.setText(data.title);
        published_at.setText(data.publishedAt);
        Picasso.get().load(data.urlToimage).into(img);
        description.setText(data.description);
        news_count.setText(String.valueOf(count+1));
    }
}
