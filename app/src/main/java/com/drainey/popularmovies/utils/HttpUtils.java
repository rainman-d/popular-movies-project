package com.drainey.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by david-rainey on 5/7/18.
 */

public class HttpUtils {

    /*  Code adopted from Sunshine project in Udacity course
    *   https://github.com/udacity/ud851-Sunshine,    NetworkUtils.java
    * */
    public static String getApiData(URL url) throws IOException{
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try{
            InputStream is = conn.getInputStream();

            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            conn.disconnect();
        }
    }

    public static boolean isNetworkConnected(Context context){
        boolean isConnected = false;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager != null){
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            isConnected = networkInfo != null && networkInfo.isConnected();
        }
        return isConnected;
    }
}
