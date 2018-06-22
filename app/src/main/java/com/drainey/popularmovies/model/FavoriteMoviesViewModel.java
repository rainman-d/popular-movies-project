package com.drainey.popularmovies.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drainey.popularmovies.persistence.AppDatabase;

import java.util.List;

/**
 * Created by david-rainey on 6/18/18.
 */

public class FavoriteMoviesViewModel extends AndroidViewModel {
    private static final String TAG = FavoriteMoviesViewModel.class.getSimpleName();

    private LiveData<List<Movie>> favoriteMovies;
    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        Log.d(TAG, "Retrieving movies from the database");
        favoriteMovies = db.movieDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies(){
        return favoriteMovies;
    }
}
