package com.example.musicsearchapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class GetMusicAsync extends AsyncTask<String, Integer, ArrayList<iTunes>>
{
    RequestParams mParams;
    MusicInterface musicInterface;
    String date;

    public GetMusicAsync(RequestParams mParams, MusicInterface musicInterface) {
        this.mParams = mParams;
        this.musicInterface = musicInterface;
    }

    @Override
    protected void onPreExecute() {
        musicInterface.doPreExecute();
    }

    @Override
    protected ArrayList<iTunes> doInBackground(String... strings) {
        ArrayList<iTunes> result = new ArrayList<>();
        try
        {
            URL url = new URL(mParams.getEncodedUrl(strings[0]));
            Log.d("demo","URL" +url);
            trustEveryone();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                Log.d("demo","JSON"+json.toString());
                JSONObject root = new JSONObject(json);
                JSONArray results = root.getJSONArray("results");
                for(int i = 0; i < results.length(); i++)
                {
                    publishProgress(i);
                    JSONObject resultJson = results.getJSONObject(i);
                    iTunes itunes = new iTunes();
                    itunes.trackName = resultJson.getString("trackName");
                    itunes.genre = resultJson.getString("primaryGenreName");
                    itunes.album = resultJson.getString("collectionName");
                    itunes.artist = resultJson.getString("artistName");
                    itunes.trackPrice = Double.parseDouble(resultJson.getString("trackPrice"));
                    itunes.albumPrice = resultJson.getString("collectionPrice");
                    date = resultJson.getString("releaseDate");
                    //2005-03-01T08:00:00Z
                    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date newDate = spf.parse(date);
                    spf = new SimpleDateFormat("MM-dd-yyyy");
                    date = spf.format(newDate);
                    itunes.date = date;
                    itunes.urlToImage = resultJson.getString("artworkUrl100");
                    Log.d("demo","Results "+itunes);
                    result.add(itunes);
                }
            }
            else
            {
                Log.d("demo","Connection: "+connection.getResponseCode());
            }
        }catch(Exception e)
        {
            Log.d("demo","error: "+e);
        }
        return result;
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

    @Override
    protected void onPostExecute(ArrayList<iTunes> iTunes) {
        Log.d("demo", "arraylist: "+iTunes);
        musicInterface.doPostExecute(iTunes);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        musicInterface.updateProgress(values);
    }

    public static interface MusicInterface
    {
        public void doPostExecute(ArrayList<iTunes> iTunes);
        public void doPreExecute();
        public void updateProgress(Integer... values);

    }
}
