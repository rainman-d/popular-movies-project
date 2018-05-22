package com.drainey.popularmovies;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.drainey.popularmovies.model.Movie;
import com.drainey.popularmovies.utils.ImageUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private Movie movie;
    private TextView mOriginalTitleTextView;
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
        mOriginalTitleTextView = (TextView) findViewById(R.id.tv_original_title);
        if(getIntent().getExtras() != null) {
            movie = getIntent().getExtras().getParcelable(MainActivity.MOVIE_PARCEL);
            loadDetails();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
