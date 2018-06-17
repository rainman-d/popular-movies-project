package com.drainey.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.drainey.popularmovies.BuildConfig;
import com.drainey.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david-rainey on 5/10/18.
 */

public class MovieDataUtils {
    public static final String POPULAR_MOVIE_PATH = "popular";
    public static final String TOP_RATED_MOVIE_PATH = "top_rated";
    public static final String API_KEY = "api_key";
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String API_VIDEOS_PATH = "videos";
    public static final String API_REVIEWS_PATH = "reviews";
    public static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch";
    public static final String API_KEY_VALUE = BuildConfig.MOVIE_DB_API_KEY;

    public static URL buildYoutubeUrl(String trailerId){
        Uri uri = Uri.parse(YOUTUBE_VIDEO_URL).buildUpon()
                .appendQueryParameter("v", trailerId)
                .build();
        URL url = convertUriToUrl(uri);
        return url;
    }

    public static URL convertUriToUrl(Uri uri){
        URL returnUrl = null;
        try{
            returnUrl = new URL(uri.toString());
        } catch (MalformedURLException e){
            Log.e(MovieDataUtils.class.getSimpleName(), "Error creating URL from uri supplied: " + uri.toString(), e);
        }

        return returnUrl;
    }

    public static URL buildMovieCall(int id, String path){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(path)
                .appendQueryParameter(API_KEY, API_KEY_VALUE)
                .build();
        URL url = convertUriToUrl(uri);
        return url;
    }

    public static URL buildApiCall(String path, String key){

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(path)
                .appendQueryParameter(API_KEY, key)
                .build();

        URL url = convertUriToUrl(uri);

        return url;
    }


}
