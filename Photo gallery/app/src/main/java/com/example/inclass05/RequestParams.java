package com.example.inclass05;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class RequestParams {
    private HashMap<String,String> params;
    private StringBuilder stringBuilder;

    public RequestParams() {
        params = new HashMap<>();
        stringBuilder = new StringBuilder();
    }

    public RequestParams addParameter(String key,String value)  {
        try {
            params.put(key, URLEncoder.encode(value,"UTF-8"));
            Log.d("demo","Params Hashmap" +params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }
    public String getEncodedParameters(){
        for(String key : params.keySet())
        {
            stringBuilder.append(key + "=" + params.get(key));
        }
        Log.d("demo","encodedparameter"+stringBuilder);
        return stringBuilder.toString();
    }
    public String getEncodedUrl(String Url)
    {
        //Log.d("demo","getencodedurl"+Url+"?"+getEncodedParameters());
        return (Url + "?" + getEncodedParameters());

    }


}
