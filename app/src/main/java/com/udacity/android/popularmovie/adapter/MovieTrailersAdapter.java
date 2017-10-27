package com.udacity.android.popularmovie.adapter;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.android.popularmovie.R;
import com.udacity.android.popularmovie.data.MovieTrailer;
import com.udacity.android.popularmovie.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder>{
    public MovieTrailer getMovieTrailersData(int idx) {
        return mMovieTrailersData.get(idx);
    }

    private ArrayList<MovieTrailer> mMovieTrailersData = new ArrayList<>();
    private final OnClickMovieTrailerHandler mClickHandler;

    public interface OnClickMovieTrailerHandler{
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
        public void onClick(View trailerView) {
            int finalRadious = (int) Math.hypot(trailerView.getWidth() / 2, trailerView.getHeight() / 2);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator anim = ViewAnimationUtils.createCircularReveal(trailerView,
                        (int) trailerView.getWidth() / 2,
                        (int) trailerView.getHeight() / 2,
                        0, finalRadious);
                anim.start();
            }
            mClickHandler.onMovieTrailerClick(getAdapterPosition());
        }
    }
}
