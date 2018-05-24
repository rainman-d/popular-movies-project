package com.drainey.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.drainey.popularmovies.model.Movie;
import com.drainey.popularmovies.utils.HttpUtils;
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
    public static final String MOVIE_PARCEL = "movie_parcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mErrorIcon.setImageResource(R.drawable.ic_error_icon);
        int color = ContextCompat.getColor(this, R.color.dark_red);
        mErrorIcon.setColorFilter(color);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter == null){
            if(this.isNetworkConnected()) {
                URL url = MovieDataUtils.buildApiCall(MovieDataUtils.POPULAR_MOVIE_PATH, MovieDataUtils.API_KEY_VALUE);
                new MovieTask().execute(url);
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
            final List<Movie> movieList = MovieDataUtils.getMovieList(s);
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
        boolean isConnectedToNetwork = isNetworkConnected();
        if(!isConnectedToNetwork){
            toggleErrorMessage(true);
            return true;
        } else {
            toggleErrorMessage(false);
        }

        switch (item.getItemId()){
            case R.id.popular_movies:
                apiPath = MovieDataUtils.POPULAR_MOVIE_PATH;
                break;
            case R.id.top_rated:
                apiPath = MovieDataUtils.TOP_RATED_MOVIE_PATH;
                break;
        }

        if(apiPath != null){
            URL url = MovieDataUtils.buildApiCall(apiPath, MovieDataUtils.API_KEY_VALUE);
            new MovieTask().execute(url);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkConnected(){
        boolean isConnected = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager != null){
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            isConnected = networkInfo != null && networkInfo.isConnected();
        }
        return isConnected;
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
}
