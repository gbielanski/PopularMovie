package com.udacity.android.popularmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;
import static java.lang.System.load;

/**
 * Created by Grzegorz on 01.04.2017.
 */

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder> {

    ArrayList<MovieData> mMovieData = new ArrayList<>();

    private final OnClickMoviePosterHandler mClickHandler;
    private final String path = "http://image.tmdb.org/t/p/";
    private final String imgSize = "w342";

    public MoviePosterAdapter(OnClickMoviePosterHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    interface OnClickMoviePosterHandler {
        void moviePosterOnClick(int possition);
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
        MovieData movieData = mMovieData.get(position);
        Picasso.with(holder.mPosterImageView.getContext())
                .load(path+imgSize+movieData.getPoster_path())
                .into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    public void addMovieData(ArrayList<MovieData> movieData){
        mMovieData = movieData;
        notifyDataSetChanged();
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
            mClickHandler.moviePosterOnClick(getAdapterPosition());

        }
    }
}
