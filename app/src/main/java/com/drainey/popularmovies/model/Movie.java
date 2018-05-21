package com.drainey.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by david-rainey on 5/7/18.
 */

public class Movie implements Parcelable{
    private String title;
    private String movieId;
    private String imageUrl;
    private String overview;
    private String backdropPath;
    private String movieRating;
    private String releaseDate;

    public Movie(){

    }

    public Movie(Parcel in){
        ReadFromParcel(in);
    }

    public Movie(String title, String movieId, String imageUrl, String overview, String backdropPath, String movieRating, String releaseDate) {
        this.title = title;
        this.movieId = movieId;
        this.imageUrl = imageUrl;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.movieRating = movieRating;
        this.releaseDate = releaseDate;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in ) {
            return new Movie( in );
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private void ReadFromParcel(Parcel in){
        title = in.readString();
        movieId = in.readString();
        imageUrl = in.readString();
        overview = in.readString();
        backdropPath = in.readString();
        movieRating = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(movieId);
        parcel.writeString(imageUrl);
        parcel.writeString(overview);
        parcel.writeString(backdropPath);
        parcel.writeString(movieRating);
        parcel.writeString(releaseDate);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", movieId='" + movieId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", overview='" + overview + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", movieRating='" + movieRating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
