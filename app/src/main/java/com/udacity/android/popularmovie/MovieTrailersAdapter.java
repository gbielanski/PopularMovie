package com.udacity.android.popularmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.android.popularmovie.data.MovieTrailer;
import com.udacity.android.popularmovie.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder>{
    public MovieTrailer getMovieTrailersData(int idx) {
        return mMovieTrailersData.get(idx);
    }

    private ArrayList<MovieTrailer> mMovieTrailersData = new ArrayList<>();
    private final OnClickMovieTrailerHandler mClickHandler;

    interface OnClickMovieTrailerHandler{
        void onMovieTrailerClick(int position);
    }

    public MovieTrailersAdapter(OnClickMovieTrailerHandler clickHandler){
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.trailers_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        MovieTrailer movieTrailer = mMovieTrailersData.get(position);
        Picasso.with(holder.mTrailerImg.getContext())
                .load(NetworkUtils.getUrlMovieTrailerPreview(movieTrailer.getTrailerKey()))
                .placeholder(R.drawable.placeholder)
                .into(holder.mTrailerImg);
    }

    @Override
    public int getItemCount() {
        return mMovieTrailersData.size();
    }

    public void setMovieTrailersData(ArrayList<MovieTrailer> mMovieTrailersData) {
        this.mMovieTrailersData = mMovieTrailersData;
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.img_trailer_item) ImageView mTrailerImg;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onMovieTrailerClick(getAdapterPosition());
        }
    }
}
