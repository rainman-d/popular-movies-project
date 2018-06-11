package com.drainey.popularmovies;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drainey.popularmovies.model.Movie;
import com.drainey.popularmovies.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private Movie movie;
    @BindView(R.id.tv_detail_movie_title) TextView mTitleTextView;
    @BindView(R.id.tv_original_title) TextView mOriginalTitleTextView;
    @BindView(R.id.iv_movie_image) ImageView mMovieImageView;
    @BindView(R.id.tv_rating) TextView mRatingTextView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_overview) TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

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

}
