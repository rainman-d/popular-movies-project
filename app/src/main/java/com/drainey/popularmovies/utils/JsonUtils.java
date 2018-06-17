package com.drainey.popularmovies.utils;

import android.util.Log;

import com.drainey.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david-rainey on 6/16/18.
 */

public class JsonUtils {
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
    private static final String JSON_REVIEW_CONTENT_KEY = "content";
    private static final String JSON_VIDEO_KEY = "key";
    private static final String API_REVIEWS = MovieDataUtils.API_REVIEWS_PATH;
    private static final String API_VIDEOS = MovieDataUtils.API_VIDEOS_PATH;

    private static List<JSONObject> getJsonObjectList(String jsonString){
        List<JSONObject> jsonObjects = new ArrayList<>();
        if(jsonString != null && jsonString.trim().length() != 0){
            try{
                JSONObject obj = new JSONObject(jsonString);
                JSONArray array = obj.getJSONArray(JSON_KEY_RESULTS);

                for(int i = 0; i < array.length(); i++){
                    jsonObjects.add(array.getJSONObject(i));
                }
            } catch (JSONException e){
                Log.e(JsonUtils.class.getSimpleName(), "Error parsing json data", e);
            }
        }
        return jsonObjects;
    }

    public static List<String> getMovieDetailsDataList(String jsonString, String path){
        List<String> list = new ArrayList<>();
        List<JSONObject> objectList = getJsonObjectList(jsonString);
        if(objectList != null && !objectList.isEmpty()){
            switch (path) {
                case API_VIDEOS:
                    list = buildSingleValuesList(objectList, JSON_VIDEO_KEY);
                    break;
                case API_REVIEWS:
                    list = buildSingleValuesList(objectList, JSON_REVIEW_CONTENT_KEY);
                    break;
                default:
                    Log.e(JsonUtils.class.getSimpleName(), "Incorrect input path " + path);
                    break;
            }
        }
        return list;
    }

    public static List<Movie> getMovieList(String jsonString){
        List<Movie> movieList = new ArrayList<>();
        List<JSONObject> objectList = getJsonObjectList(jsonString);
        if(objectList != null && !objectList.isEmpty()) {
            movieList = buildMovieList(objectList);
        }
        return movieList;
    }

    private static List<String> buildSingleValuesList(List<JSONObject> jsonObjects, String key){
        List<String> trailerList = new ArrayList<>();
        for(int i = 0; i < jsonObjects.size(); i++){
            try {
                JSONObject obj = jsonObjects.get(i);
                String trailerId = obj.getString(key);
                trailerList.add(trailerId);
            } catch (JSONException e){
                Log.e(JsonUtils.class.getSimpleName(), "error parsing json data", e);
            }
        }
        return trailerList;
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
                Log.e(JsonUtils.class.getSimpleName(), "error parsing json data", e);
            }

            if(movie != null){
                movieList.add(movie);
            }

        }
        return movieList;
    }
}
