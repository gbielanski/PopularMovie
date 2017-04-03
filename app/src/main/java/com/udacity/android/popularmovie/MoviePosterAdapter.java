package com.udacity.android.popularmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Grzegorz on 01.04.2017.
 */

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder> {

    private final OnClickMoviePosterHandler mClickHandler;

    public MoviePosterAdapter(OnClickMoviePosterHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    interface OnClickMoviePosterHandler {
        void moviePosterOnClick();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context ctx = parent.getContext();
        int posterGridItemId = R.layout.poster_grid_item;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(posterGridItemId, parent, false);
        MovieViewHolder holder = new MovieViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView mPosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView  = (ImageView)itemView.findViewById(R.id.img_poster_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.moviePosterOnClick();
        }
    }
}
