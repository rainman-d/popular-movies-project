package com.drainey.popularmovies;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drainey.popularmovies.model.Movie;
import com.drainey.popularmovies.utils.HttpUtils;
import com.drainey.popularmovies.utils.ImageUtils;
import com.drainey.popularmovies.utils.JsonUtils;
import com.drainey.popularmovies.utils.MovieDataUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Map<String,String>> {
    private Movie movie;
    @BindView(R.id.tv_detail_movie_title) TextView mTitleTextView;
    @BindView(R.id.tv_original_title) TextView mOriginalTitleTextView;
    @BindView(R.id.iv_movie_image) ImageView mMovieImageView;
    @BindView(R.id.tv_rating) TextView mRatingTextView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_overview) TextView mOverviewTextView;
    @BindView(R.id.rv_trailers) RecyclerView trailerRecyclerView;
    @BindView(R.id.rv_reviews) RecyclerView reviewsRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    public static final int MOVIE_DATA_LOADER_ID = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if(getIntent().getExtras() != null) {
            movie = getIntent().getExtras().getParcelable(MainActivity.MOVIE_PARCEL);
            loadDetails();
        }

//        trailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this){
//            , LinearLayoutManager.VERTICAL, false
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        trailerAdapter = new TrailerAdapter(this);
        reviewAdapter = new ReviewAdapter(this);

        trailerRecyclerView.setAdapter(trailerAdapter);
        reviewsRecyclerView.setAdapter(reviewAdapter);

//        new TrailerTask().execute();
        getSupportLoaderManager().initLoader(MOVIE_DATA_LOADER_ID, null, this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(MOVIE_DATA_LOADER_ID, null, this);
    }

    private void loadDetails(){
        mTitleTextView.setText(this.movie.getTitle());
        int posterWidth = (int)getResources().getDimension(R.dimen.detail_poster_width);
        int posterHeight = (int)getResources().getDimension(R.dimen.detail_poster_height);
        Drawable errorIcon = ImageUtils.getRedErrorIcon(this);

        Picasso.with(this)
                .load(this.movie.getImageUrl())
                .error(errorIcon)
                .resize(posterWidth, posterHeight)
                .into(mMovieImageView);
        String rating = movie.getMovieRating() + getString(R.string.out_of_ten);
        mRatingTextView.setText(rating);
        int year = Integer.parseInt(this.movie.getReleaseDate().substring(0, 4));
        int month = Integer.parseInt(this.movie.getReleaseDate().substring(5, 7));
        int day = Integer.parseInt(this.movie.getReleaseDate().substring(8, 10));
        mReleaseDateTextView.setText(month + "-" + day + "-" + year);
        mOverviewTextView.setText(this.movie.getOverview());
        mOriginalTitleTextView.setText(this.movie.getOriginalTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addFavorite(View view){
        Toast.makeText(this, this.movie.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public Loader<Map<String,String>> onCreateLoader(int i, final Bundle bundle) {

        return new AsyncTaskLoader<Map<String,String>>(this) {

            Map<String,String> mMovieData = null;
            @Override
            public Map<String,String> loadInBackground() {
                Integer movieId = Integer.parseInt(DetailActivity.this.movie.getMovieId());
                URL trailerUrl = MovieDataUtils.buildMovieCall(movieId, MovieDataUtils.API_VIDEOS_PATH);
                URL reviewsUrl = MovieDataUtils.buildMovieCall(movieId, MovieDataUtils.API_REVIEWS_PATH);
                Map<String,String> dataMap = new HashMap<>();
                try {
                    String trailerJsonData = HttpUtils.getApiData(trailerUrl);
                    String reviewsJsonData = HttpUtils.getApiData(reviewsUrl);
                    dataMap.put(MovieDataUtils.API_VIDEOS_PATH, trailerJsonData);
                    dataMap.put(MovieDataUtils.API_REVIEWS_PATH, reviewsJsonData);
                } catch (IOException e){
                    Log.e(DetailActivity.class.getSimpleName(), "Error retrieving movie data", e);
                }
                return dataMap;
            }

            @Override
            protected void onStartLoading() {
                if(mMovieData != null){
                    deliverResult(mMovieData);
                }else{
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(Map<String, String> data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Map<String,String>> loader, Map<String,String> jsonDataMap) {
        List<String> trailers = JsonUtils.getMovieDetailsDataList(jsonDataMap.get(MovieDataUtils.API_VIDEOS_PATH), MovieDataUtils.API_VIDEOS_PATH);
        List<String> reviews = JsonUtils.getMovieDetailsDataList(jsonDataMap.get(MovieDataUtils.API_REVIEWS_PATH), MovieDataUtils.API_REVIEWS_PATH);
        trailerAdapter.changeTrailerList(trailers);
        reviewAdapter.changeReviewList(reviews);
        trailerAdapter.notifyDataSetChanged();
        reviewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Map<String,String>> loader) {

    }

//    class TrailerTask extends AsyncTask<Integer, Void, String>{
//
//        @Override
//        protected String doInBackground(Integer... id) {
//            String movieDbData;
//            Integer movieId = Integer.parseInt(DetailActivity.this.movie.getMovieId());
//            URL url = MovieDataUtils.buildMovieCall(movieId, MovieDataUtils.API_VIDEOS_PATH);
//            try{
//                movieDbData = HttpUtils.getApiData(url);
//            }catch (IOException e){
//                Log.e(DetailActivity.class.getSimpleName(), "Error retrieving data from API", e);
//                movieDbData = e.toString();
//            }
//
//            return movieDbData;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//
//            List<String> list = JsonUtils.getTrailerList(s);
//            Log.d(DetailActivity.class.getSimpleName(), "Returned info: " + list.toString());
//            trailerAdapter.changeTrailerList(list);
//        }
//    }
}
