package com.drainey.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drainey.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david-rainey on 6/17/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private List<String> reviewsList;
    Context mContext;

    public ReviewAdapter(Context mContext) {
        this.reviewsList = new ArrayList<>();
        this.mContext = mContext;
    }

    public ReviewAdapter(List<String> reviews, Context mContext) {
        this.reviewsList = reviews;
        this.mContext = mContext;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentNow = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentNow);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view, mContext);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.reviewsList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView listItemView;
        Context context;

        public ReviewViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context = context;

            listItemView = (TextView) itemView.findViewById(R.id.tv_review_item);
        }

        void bind(int index){
            listItemView.setText(reviewsList.get(index));
        }
    }

    public void changeReviewList(List<String> newList){
        if(newList != null && !newList.isEmpty()){
             this.reviewsList = newList;
        }
    }
}
