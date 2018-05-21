package com.drainey.popularmovies;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drainey.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private Movie movie;
    private TextView mTitleTextView;
    private ImageView mMovieImageView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTitleTextView = (TextView)findViewById(R.id.tv_detail_movie_title);
        mMovieImageView = (ImageView)findViewById(R.id.iv_movie_image);
        mRatingTextView = (TextView)findViewById(R.id.tv_rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mOverviewTextView = (TextView) findViewById(R.id.tv_overview);
        movie = getIntent().getExtras().getParcelable(MainActivity.MOVIE_PARCEL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadDetails();

    }

    private void loadDetails(){
        mTitleTextView.setText(this.movie.getTitle());
        int posterWidth = (int)getResources().getDimension(R.dimen.detail_poster_width);
        int posterHeight = (int)getResources().getDimension(R.dimen.detail_poster_height);
        Picasso.with(this)
                .load(this.movie.getImageUrl())
                .resize(posterWidth, posterHeight)
                .into(mMovieImageView);
        String rating = movie.getMovieRating() + getString(R.string.out_of_ten);
        mRatingTextView.setText(rating);
        mReleaseDateTextView.setText(this.movie.getReleaseDate().substring(0, 4));
        mOverviewTextView.setText(this.movie.getOverview());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
