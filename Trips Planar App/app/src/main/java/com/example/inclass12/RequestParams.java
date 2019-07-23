package com.example.inclass12;

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
            Log.d("demo","add parameter exception: "+e);
        }
        return this;
    }
    public String getEncodedParameters(){
        for(String key : params.keySet())
        {
            if(stringBuilder.length() > 0)
            {
                stringBuilder.append("&");
            }
            stringBuilder.append(key + "=" + params.get(key));
        }
        Log.d("demo","encodedparameter"+stringBuilder);
        return stringBuilder.toString();
    }
    public String getEncodedUrl(String Url)
    {
        String encodedUrl = (Url + "?" + getEncodedParameters());
        Log.d("demo","EncodedUrl "+encodedUrl);
        return encodedUrl;

    }
}
