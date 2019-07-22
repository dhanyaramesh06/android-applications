package com.example.inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetImageAsync extends AsyncTask<String, Void, Void> {
    SetUIComp setUIComp;
    Bitmap bitmap = null;

    public GetImageAsync(SetUIComp setUIComp) {
        this.setUIComp = setUIComp;
    }

    @Override
    protected void onPreExecute() {
        setUIComp.setLoading();
    }

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection = null;
        bitmap = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        setUIComp.setUICompStatus(bitmap);
    }

    public static interface SetUIComp
    {
        public void setUICompStatus(Bitmap bitmap);
        public void setLoading();
    }


}
