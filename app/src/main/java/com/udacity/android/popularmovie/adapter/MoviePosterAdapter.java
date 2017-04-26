package com.udacity.android.popularmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.android.popularmovie.R;
import com.udacity.android.popularmovie.data.MovieData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.android.popularmovie.utils.MovieUtils.IMG_SIZE;
import static com.udacity.android.popularmovie.utils.MovieUtils.PATH;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder> {

    private ArrayList<MovieData> mMovieData = new ArrayList<>();

    private final OnClickMoviePosterHandler mClickHandler;

    public ArrayList<MovieData> getMovieData() {
        return mMovieData;
    }

    public MoviePosterAdapter(OnClickMoviePosterHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public interface OnClickMoviePosterHandler {
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
                .error(R.drawable.image_not_available)
                .placeholder(R.drawable.placeholder)
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

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.img_poster_item) ImageView mPosterImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.moviePosterOnClick(getAdapterPosition());
        }
    }
}
