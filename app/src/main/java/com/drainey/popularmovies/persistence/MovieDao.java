package com.drainey.popularmovies.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.drainey.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by david-rainey on 6/17/18.
 */
@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie ORDER BY title")
    LiveData<List<Movie>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE movie_id = :movieId")
    Movie loadSingleMovie(String movieId);

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("DELETE FROM movie WHERE movie_id = :movieId")
    void deleteMovie(String movieId);
}
