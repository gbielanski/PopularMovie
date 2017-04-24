package com.udacity.android.popularmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.android.popularmovie.data.MovieReview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder>{
    private List<MovieReview> mReviewsData = new ArrayList<>();
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.reviews_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        MovieReview movieReview = mReviewsData.get(position);
        holder.author.setText(movieReview.getAuthor());
        holder.review.setText(movieReview.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewsData.size();
    }

    public void setReviewsData(List<MovieReview> reviewsData){
        mReviewsData = reviewsData;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author) TextView author;
        @BindView(R.id.review) TextView review;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
