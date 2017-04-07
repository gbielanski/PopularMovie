package com.udacity.android.popularmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.udacity.android.popularmovie.MovieUtils.IMG_SIZE;
import static com.udacity.android.popularmovie.MovieUtils.PATH;

class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder> {

    ArrayList<MovieData> mMovieData = new ArrayList<>();
    private final OnClickMoviePosterHandler mClickHandler;

    MoviePosterAdapter(OnClickMoviePosterHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    interface OnClickMoviePosterHandler {
        void moviePosterOnClick(int position);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context ctx = parent.getContext();
        int posterGridItemId = R.layout.poster_grid_item;
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(posterGridItemId, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieData movieData = mMovieData.get(position);
        Picasso.with(holder.mPosterImageView.getContext())
                .load(PATH + IMG_SIZE + movieData.getPosterPath())
                .into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    void addMovieData(ArrayList<MovieData> movieData){
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView mPosterImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView  = (ImageView)itemView.findViewById(R.id.img_poster_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.moviePosterOnClick(getAdapterPosition());

        }
    }
}
