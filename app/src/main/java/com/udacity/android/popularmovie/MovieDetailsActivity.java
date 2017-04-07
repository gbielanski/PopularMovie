package com.udacity.android.popularmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.udacity.android.popularmovie.MovieUtils.EXTRA_MOVIE_DETAILS;
import static com.udacity.android.popularmovie.MovieUtils.IMG_SIZE;
import static com.udacity.android.popularmovie.MovieUtils.PATH;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        MovieData movieData = getIntent().getParcelableExtra(EXTRA_MOVIE_DETAILS);

        TextView tv_movie_title = (TextView)findViewById(R.id.tv_movie_title);
        tv_movie_title.setText(movieData.getOriginalTitle());

        TextView tv_movie_release_date = (TextView)findViewById(R.id.tv_movie_release_date);
        tv_movie_release_date.setText(movieData.getReleaseDate());

        ImageView imgMoviePoster = (ImageView)findViewById(R.id.img_poster_item_in_details);
        Picasso.with(this).load(PATH + IMG_SIZE + movieData.getPosterPath()).into(imgMoviePoster);

        TextView tv_rating = (TextView)findViewById(R.id.tv_rating);
        tv_rating.setText(movieData.getVoteAverage().toString());

        RatingBar rbRating = (RatingBar) findViewById(R.id.rb_rating);
        rbRating.setRating(movieData.getVoteAverage().floatValue());

        TextView tvOverview = (TextView)findViewById(R.id.tv_overview);
        tvOverview.setText(movieData.getOverview());
    }
}
