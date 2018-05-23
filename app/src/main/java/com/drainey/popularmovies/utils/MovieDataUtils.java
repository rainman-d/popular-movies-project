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
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w342";
    public static final String JSON_KEY_RESULTS = "results";
    public static final String JSON_KEY_TITLE = "title";
    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_POSTER_PATH = "poster_path";
    public static final String JSON_KEY_VOTE_AVG = "vote_average";
    public static final String JSON_KEY_OVERVIEW = "overview";
    public static final String JSON_KEY_RELEASE_DATE = "release_date";
    public static final String JSON_KEY_BACKDROP_PATH = "backdrop_path";
    public static final String JSON_KEY_ORIGINAL_TITLE = "original_title";
    public static final String API_KEY_VALUE = BuildConfig.MOVIE_DB_API_KEY;


    public static URL buildApiCall(String path, String key){

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(path)
                .appendQueryParameter(API_KEY, key)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            Log.e(MovieDataUtils.class.getSimpleName(), "Bad Url string supplied!", e);
        }

        return url;
    }

    public static List<Movie> getMovieList(String jsonString){
        List<Movie> movieList = new ArrayList<>();
        if(jsonString != null && jsonString.trim().length() != 0){
            try {
                JSONObject object = new JSONObject(jsonString);
                JSONArray array = object.getJSONArray(JSON_KEY_RESULTS);
                List<JSONObject> jsonObjects = new ArrayList<>();
                for(int i = 0; i < array.length(); i++){
                    jsonObjects.add(array.getJSONObject(i));
                }
                movieList = buildMovieList(jsonObjects);
            }catch (JSONException e){
                Log.e(MovieDataUtils.class.getSimpleName(), "Error parsing json data", e);
            }
        }
        return movieList;
    }

    public static List<Movie> buildMovieList(List<JSONObject> jsonObjects){
        List<Movie> movieList = new ArrayList<>();

        for(int i = 0; i < jsonObjects.size(); i++){
            Movie movie = null;
            try {
                JSONObject obj = jsonObjects.get(i);
                String title =  obj.getString(JSON_KEY_TITLE);
                String id = obj.getString(JSON_KEY_ID);
                String imagePath = IMAGE_BASE_URL + obj.getString(JSON_KEY_POSTER_PATH);
                String backdropPath = BACKDROP_BASE_URL + obj.getString(JSON_KEY_BACKDROP_PATH);
                String rating = obj.getString(JSON_KEY_VOTE_AVG);
                String overview = obj.getString(JSON_KEY_OVERVIEW);
                String releaseDate = obj.getString(JSON_KEY_RELEASE_DATE);
                String originalTitle = obj.getString(JSON_KEY_ORIGINAL_TITLE);
                movie = new Movie(title, id, imagePath, overview, backdropPath, rating, releaseDate, originalTitle);

            } catch (JSONException e){
                Log.e(MovieDataUtils.class.getSimpleName(), "error parsing json data", e);
            }

            if(movie != null){
                movieList.add(movie);
            }

        }
        return movieList;
    }
}
