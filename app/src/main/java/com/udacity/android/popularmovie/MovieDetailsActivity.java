package com.udacity.android.popularmovie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private final String path = "http://image.tmdb.org/t/p/";
    private final String imgSize = "w342";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        MovieData movieData = getIntent().getParcelableExtra("MOVIE_DETAILS");
        Log.v("DetMovieData", movieData.toString());

        TextView tv_movie_title = (TextView)findViewById(R.id.tv_movie_title);
        tv_movie_title.setText(movieData.getOriginal_title());

        TextView tv_movie_releasa_date = (TextView)findViewById(R.id.tv_movie_release_date);
        tv_movie_releasa_date.setText(movieData.getRelease_date());

        ImageView imgMoviePoster = (ImageView)findViewById(R.id.img_poster_item_in_details);
        Picasso.with(this).load(path+imgSize+movieData.getPoster_path()).into(imgMoviePoster);

        TextView tv_rating = (TextView)findViewById(R.id.tv_rating);
        tv_rating.setText("" + movieData.getVote_average());

        RatingBar rbRating = (RatingBar) findViewById(R.id.rb_rating);
        rbRating.setRating(movieData.getVote_average().floatValue());

        TextView tvOverview = (TextView)findViewById(R.id.tv_overview);
        tvOverview.setText(movieData.getOverview());




    }
}
