package com.example.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

public class GetNewsAsync extends AsyncTask<String, Void, ArrayList<Articles>> {
    RequestParams mparams;
    SetUIComp set;

    public GetNewsAsync(RequestParams mparams, SetUIComp set) {
        this.mparams = mparams;
        this.set = set;
    }

    @Override
    protected void onPreExecute()
    {
        set.setUIpreexecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Articles> result)
    {
        set.setUIpostexecute(result);
    }

    @Override
    protected ArrayList<Articles> doInBackground(String... strings) {
        ArrayList<Articles> result = new ArrayList<>();
        try
        {
            URL url = new URL(mparams.getEncodedUrl(strings[0]));
            Log.d("demo","URL" +url);
            trustEveryone();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                Log.d("demo","JSON"+json.toString());
                JSONObject root = new JSONObject(json);
                JSONArray articles = root.getJSONArray("articles");
                for(int i = 0; i < articles.length(); i++)
                {
                    JSONObject articleJson = articles.getJSONObject(i);
                    Articles a = new Articles();
                    a.title = articleJson.getString("title");
                    a.publishedAt = articleJson.getString("publishedAt");
                    a.urlToimage = articleJson.getString("urlToImage");
                    a.description = articleJson.getString("description");
                    Log.d("demo","Articles"+a);
                    result.add(a);
                }
            }
            else
            {
                Log.d("demo","Connection: "+connection.getResponseCode());
            }
        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }catch(IOException e)
        {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

        /*HttpURLConnection connection = null;
        String result = null;
        try {
            String strUrl = "https://newsapi.org/v2/top-headlines?category=Science&apiKey=18d8728f08dc4ec4ba86de439f5d2711&country=us";
            URL url = new URL(strUrl);
            trustEveryone();
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = IOUtils.toString(connection.getInputStream(), "UTF8");
            }
        } catch (Exception e) {
            Log.d("demo","exception "+e);
            //Handle the exceptions
        } finally {
            //Close open connections and reader
        }
        Log.d("demo","test "+result);
        return null;*/

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

    public static interface SetUIComp
    {
        public void setUIpreexecute();
        public void setUIpostexecute(ArrayList<Articles> arrayList);
    }
}
