package com.example.recipepuppy;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesScreen extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String dish;
    ArrayList<String> ingredients;
    ArrayList<Recipes> result;
    Button finish;
    RecipeInterface object;
    TextView loading;
    ProgressBar progressBar;
    boolean flag = false;
    public RecipesScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes_screen, container, false);
        finish = view.findViewById(R.id.bt_finish);
        mRecyclerView = view.findViewById(R.id.recipeView);
        loading = view.findViewById(R.id.loading);
        progressBar = view.findViewById(R.id.progressBar);
        loading.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(isConnected()) {
            Toast.makeText(getContext(), "Internet connected", Toast.LENGTH_SHORT).show();
            Bundle b = this.getArguments();
            if (b != null) {
                dish = (String) b.getSerializable("dish");
                ingredients = (ArrayList) b.getSerializable("list");
                Log.d("demo", "In recipes fragment - dish name: " + dish);
                Log.d("demo", "In recipes fragment - ingredients: " + ingredients);
                StringBuilder ingredient_params = new StringBuilder();
                int listsize = ingredients.size();
                if (listsize != 0) {
                    for (int i = 0; i < listsize; i++) {
                        if (i == listsize - 1)
                            ingredient_params.append(ingredients.get(i));
                        else {
                            ingredient_params.append(ingredients.get(i));
                            ingredient_params.append(",");
                        }
                    }
                }
                Log.d("demo", "ingredient params: " + ingredient_params.toString());
                //http:// www.recipepuppy.com/api/?i=onions,garlic&q=omelet .
                String url = "http://www.recipepuppy.com/api/?i=" + ingredient_params.toString() + "&q=" + dish;
                Log.d("demo", "url: " + url);
                new GetRecipeAsync().execute(url);
                mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                mRecyclerView.setLayoutManager(mLayoutManager);
                Log.d("demo", "Before adapter set");


            }
        }
    }
    public void onFinish(){
        if(flag==true){
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("demo","On the click of back button");
                    object.backToActivity();

                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            object = (RecipeInterface) context;
        }catch (Exception e)
        {
            Log.d("demo", "search screen on attach:"+e);
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


    public class GetRecipeAsync extends AsyncTask<String, Integer, ArrayList<Recipes>>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("demo","In pre execute");
            loading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<Recipes> doInBackground(String... strings) {
            Log.d("demo","In do in background");
            HttpURLConnection connection = null;
            result = new ArrayList<>();
            try
            {
                URL url = new URL(strings[0]);
                trustEveryone();
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray recipes = root.getJSONArray("results");
                    progressBar.setMax(recipes.length());
                    for (int i=0;i<recipes.length();i++) {
                        JSONObject recipeJson = recipes.getJSONObject(i);
                        Recipes recipe = new Recipes();
                        recipe.title = recipeJson.getString("title");
                        recipe.ingredients = recipeJson.getString("ingredients");
                        recipe.recipe_img = recipeJson.getString("thumbnail");
                        recipe.recipe_url = recipeJson.getString("href");
                        publishProgress(i);
                        result.add(recipe);
                    }
                }
            } catch (Exception e) {
                Log.d("demo","error: "+e);
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(progressBar.getProgress()+values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Recipes> recipes) {
            super.onPostExecute(recipes);
            Log.d("demo","In post execute");
            Log.d("demo","Recipes: "+recipes);
            loading.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            flag = true;
            if(recipes.size() != 0) {
                mAdapter = new DisplayRecipesAdapter(recipes);
                mRecyclerView.setAdapter(mAdapter);
                Log.d("demo", "After adapter set");

                mAdapter.notifyDataSetChanged();
            }
            else
            {
                object.backToActivity();
                Toast.makeText(getContext(), "No recipes found", Toast.LENGTH_SHORT).show();
            }
            onFinish();
        }

        private void trustEveryone() {
            try {
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }});
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, new X509TrustManager[]{new X509TrustManager(){
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }}}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(
                        context.getSocketFactory());
            } catch (Exception e) { // should never happen
                e.printStackTrace();
            }
        }
    }

    public interface RecipeInterface{
        public void backToActivity();
    }



}
