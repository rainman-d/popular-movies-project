package com.drainey.popularmovies.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.drainey.popularmovies.R;
import com.drainey.popularmovies.model.Movie;
import com.drainey.popularmovies.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by david-rainey on 5/14/18.
 */

public class MovieAdapter extends BaseAdapter {
    private List<Movie> movieList;
    private Context context;

    public MovieAdapter(Context context, List<Movie> list){
        this.context = context;
        this.movieList = list;
    }

    public void setMovieList(List<Movie> list){
        this.movieList = list;
        notifyDataSetChanged();
    }

    public List<Movie> getMovieList(){
        return movieList;
    }

    @Override
    public int getCount() {
        return this.movieList.size();
    }

    @Override
    public Movie getItem(int i) {
        return this.movieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Movie movie = getItem(i);
        Context con = viewGroup.getContext();

        ImageView imageView;
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(con);
            inflater.inflate(R.layout.movie_thumbnail, viewGroup, false);
            imageView = new ImageView(context);
            int width = context.getResources().getDisplayMetrics().widthPixels;
            int height = context.getResources().getDisplayMetrics().heightPixels;
            String url = "";
            switch (context.getResources().getConfiguration().orientation){
                case Configuration.ORIENTATION_PORTRAIT:
                    url = movie.getImageUrl();
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    url = movie.getBackdropPath();
                    break;
            }

            Drawable errorIcon = ImageUtils.getRedErrorIcon(context);

            Picasso.with(context)
                    .load(url)
                    .centerCrop()
                    .resize(width/2, height/2)
                    .error(errorIcon)
                    .into(imageView);
        } else{
            imageView = (ImageView)view;
        }

        return imageView;
    }
}
