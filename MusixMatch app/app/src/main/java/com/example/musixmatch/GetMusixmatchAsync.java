package com.example.musixmatch;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class GetMusixmatchAsync extends AsyncTask<String, Integer, ArrayList<Musixmatch>>
{
    RequestParams mParams;
    MusicInterface musicInterface;
    String date;

    public GetMusixmatchAsync(RequestParams mParams, MusicInterface musicInterface) {
        this.mParams = mParams;
        this.musicInterface = musicInterface;
    }

    @Override
    protected void onPreExecute() {
        musicInterface.doPreExecute();
    }

    @Override
    protected ArrayList<Musixmatch> doInBackground(String... strings) {
        ArrayList<Musixmatch> result = new ArrayList<>();
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
                JSONObject message = root.getJSONObject("message");
                JSONObject body = message.getJSONObject("body");
                JSONArray track_list = body.getJSONArray("track_list");
                for(int i = 0; i < track_list.length(); i++)
                {
                    publishProgress(i);
                    JSONObject trackList_json = track_list.getJSONObject(i);
                    JSONObject track = trackList_json.getJSONObject("track");
                    Musixmatch music = new Musixmatch();
                    music.track_name = track.getString("track_name");
                    music.album_name = track.getString("album_name");
                    music.artist_name = track.getString("artist_name");
                    date = track.getString("updated_time");
                    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date newDate = spf.parse(date);
                    spf = new SimpleDateFormat("MM-dd-yyyy");
                    date = spf.format(newDate);
                    music.updated_time = date;
                    music.track_share_url = track.getString("track_share_url");
                    Log.d("demo","Results "+music);
                    result.add(music);
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
    protected void onPostExecute(ArrayList<Musixmatch> result) {
        Log.d("demo", "arraylist: "+result);
        musicInterface.doPostExecute(result);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        musicInterface.updateProgress(values);
    }

    public static interface MusicInterface
    {
        public void doPostExecute(ArrayList<Musixmatch> result);
        public void doPreExecute();
        public void updateProgress(Integer... values);

    }
}
