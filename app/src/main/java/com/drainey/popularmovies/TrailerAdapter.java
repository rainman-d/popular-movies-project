package com.drainey.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drainey.popularmovies.utils.MovieDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david-rainey on 6/11/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private List<String> trailerUrls;
    Context mContext;

    public TrailerAdapter(Context context){
        this.mContext = context;
        this.trailerUrls = new ArrayList<>();
    }

    public TrailerAdapter(Context context, List<String> data){
        this.mContext = context;
        this.trailerUrls = data;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup group, int viewType) {
        Context context = group.getContext();
        int layoutId = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentNow = false;

        View view = inflater.inflate(layoutId, group, shouldAttachToParentNow);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view, mContext);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return trailerUrls.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{
        TextView listItemView;
        Context mContext;


        public TrailerViewHolder(View itemView, final Context context) {
            super(itemView);
            this.mContext = itemView.getContext();
            listItemView = (TextView)itemView.findViewById(R.id.tv_trailer_link);
            listItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Context context1 = view.getContext();
                    if(position != RecyclerView.NO_POSITION){
                        String id = trailerUrls.get(position);
                        Uri uriString = Uri.parse(MovieDataUtils.buildYoutubeUrl(id).toString());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uriString);
                        if (browserIntent.resolveActivity(context1.getPackageManager()) != null) {
                            context1.startActivity(browserIntent);
                        }
                    }
                }
            });
        }

        void bind(int index){
            String s = mContext.getString(R.string.trailer_name);
            listItemView.setText(s + " " + (index + 1));
        }
    }

    public void changeTrailerList(List<String> newList){
        if(newList != null && !newList.isEmpty()){
            this.trailerUrls = newList;
        }
    }

}
