package com.udacity.android.popularmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.android.popularmovie.MovieUtils.EXTRA_MOVIE_DETAILS;
import static com.udacity.android.popularmovie.MovieUtils.IMG_SIZE;
import static com.udacity.android.popularmovie.MovieUtils.PATH;

public class MovieDetailsActivity extends AppCompatActivity {
    @BindView(R.id.tv_movie_title) TextView tv_movie_title;
    @BindView(R.id.tv_movie_release_date) TextView tv_movie_release_date;
    @BindView(R.id.img_poster_item_in_details) ImageView imgMoviePoster;
    @BindView(R.id.tv_rating) TextView tv_rating;
    @BindView(R.id.rb_rating) RatingBar rbRating;
    @BindView(R.id.tv_overview) TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        MovieData movieData = getIntent().getParcelableExtra(EXTRA_MOVIE_DETAILS);
        tv_movie_title.setText(movieData.getOriginalTitle());
        tv_movie_release_date.setText(movieData.getReleaseDate());
        Picasso.with(this).load(PATH + IMG_SIZE + movieData.getPosterPath()).into(imgMoviePoster);
        tv_rating.setText(movieData.getVoteAverage().toString());
        rbRating.setRating(movieData.getVoteAverage().floatValue());
        tvOverview.setText(movieData.getOverview());
    }
}
