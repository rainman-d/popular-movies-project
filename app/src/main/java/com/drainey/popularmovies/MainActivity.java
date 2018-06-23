package com.drainey.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.drainey.popularmovies.adapters.MovieAdapter;
import com.drainey.popularmovies.model.Movie;
import com.drainey.popularmovies.utils.HttpUtils;
import com.drainey.popularmovies.utils.JsonUtils;
import com.drainey.popularmovies.utils.MovieDataUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_gridview) GridView mMovieGridView;
    @BindView(R.id.tv_network_error) TextView mErrorMessageTextView;
    @BindView(R.id.iv_error_icon) ImageView mErrorIcon;

    private MovieAdapter mAdapter;
    private String loadMoviesPath;
    public static final String MOVIE_LOAD_PATH= "movieUrl";
    public static final String MOVIE_PARCEL = "movie_parcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            loadMoviesPath = savedInstanceState.getString(MOVIE_LOAD_PATH);
        }

        mErrorIcon.setImageResource(R.drawable.ic_error_icon);
        int color = ContextCompat.getColor(this, R.color.dark_red);
        mErrorIcon.setColorFilter(color);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter == null){
            if(HttpUtils.isNetworkConnected(this)) {
                String path = loadMoviesPath == null ? MovieDataUtils.POPULAR_MOVIE_PATH : loadMoviesPath;

                URL url = MovieDataUtils.buildApiCall(path, MovieDataUtils.API_KEY_VALUE);
                new MovieTask().execute(url);
            } else {
                toggleErrorMessage(true);
            }
        }
    }

    private class MovieTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String movieDbData;
            try{
                movieDbData = HttpUtils.getApiData(url);
            } catch (IOException e){
                Log.e(MainActivity.class.getSimpleName(), "Error getting data from API", e);
                movieDbData = e.toString();
            }

            return movieDbData;
        }

        @Override
        protected void onPostExecute(String s) {
            final List<Movie> movieList = JsonUtils.getMovieList(s);
            mAdapter = new MovieAdapter(MainActivity.this, movieList);
            mMovieGridView.setAdapter(mAdapter);
            mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Movie movie = movieList.get(position);
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra(MOVIE_PARCEL, movie);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String apiPath = null;
        boolean isConnectionNeeded = item.getItemId() != R.id.favorite_movies;
        boolean isConnectedToNetwork = HttpUtils.isNetworkConnected(this);
        if(isConnectionNeeded && !isConnectedToNetwork){
            toggleErrorMessage(true);
            return true;
        } else {
            toggleErrorMessage(false);
        }

        switch (item.getItemId()){
            case R.id.popular_movies:
                loadMoviesPath = MovieDataUtils.POPULAR_MOVIE_PATH;
                break;
            case R.id.top_rated:
                loadMoviesPath = MovieDataUtils.TOP_RATED_MOVIE_PATH;
                break;
            case R.id.favorite_movies:
                Intent intent = new Intent(this, FavoriteMoviesActivity.class);
                startActivity(intent);
                break;
        }

        if(loadMoviesPath != null){
            URL url = MovieDataUtils.buildApiCall(loadMoviesPath, MovieDataUtils.API_KEY_VALUE);
            new MovieTask().execute(url);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleErrorMessage(boolean isError){
        if(isError){
            mErrorIcon.setVisibility(View.VISIBLE);
            mErrorMessageTextView.setVisibility(View.VISIBLE);
            mMovieGridView.setVisibility(View.GONE);
        } else {
            mErrorIcon.setVisibility(View.GONE);
            mErrorMessageTextView.setVisibility(View.GONE);
            mMovieGridView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MOVIE_LOAD_PATH, loadMoviesPath);
    }

}
