package com.drainey.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.drainey.popularmovies.adapters.MovieAdapter;
import com.drainey.popularmovies.model.FavoriteMoviesViewModel;
import com.drainey.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.drainey.popularmovies.MainActivity.MOVIE_PARCEL;

/**
 * Created by david-rainey on 6/18/18.
 */

public class FavoriteMoviesActivity extends AppCompatActivity{
    @BindView(R.id.favorite_movie_gridview)
    GridView mMovieGridView;
    @BindView(R.id.tv_no_favorites)
    TextView mNoFavoritesMessage;

    private static final String TAG = FavoriteMoviesActivity.class.getSimpleName();
    private MovieAdapter mAdapter;
    FavoriteMoviesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite_movies);
        ButterKnife.bind(this);

        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        mMovieGridView.setAdapter(mAdapter);
        mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = mAdapter.getMovieList().get(position);
                Intent intent = new Intent(FavoriteMoviesActivity.this, DetailActivity.class);
                intent.putExtra(MOVIE_PARCEL, movie);
                startActivity(intent);
            }
        });

        viewModel = setUpViewModel();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private FavoriteMoviesViewModel setUpViewModel(){
        FavoriteMoviesViewModel viewModel = ViewModelProviders.of(this)
                .get(FavoriteMoviesViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                boolean isEmpty;
                if(movies != null && !movies.isEmpty()) {
                    isEmpty = false;
                    mAdapter.setMovieList(movies);
                } else {
                    isEmpty = true;
                }
                toggleMovieGrid(isEmpty);
            }
        });
        return viewModel;
    }

    private void toggleMovieGrid(boolean isNoFavorites){
        if(isNoFavorites){
            mNoFavoritesMessage.setVisibility(View.VISIBLE);
            mMovieGridView.setVisibility(View.GONE);
        } else {
            mNoFavoritesMessage.setVisibility(View.GONE);
            mMovieGridView.setVisibility(View.VISIBLE);
        }
    }
}
