package com.udacity.android.popularmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;
import com.udacity.android.popularmovie.adapter.MovieReviewsAdapter;
import com.udacity.android.popularmovie.adapter.MovieTrailersAdapter;
import com.udacity.android.popularmovie.data.MovieContract;
import com.udacity.android.popularmovie.data.MovieData;
import com.udacity.android.popularmovie.data.MovieReview;
import com.udacity.android.popularmovie.data.MovieTrailer;
import com.udacity.android.popularmovie.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;
import static android.support.v7.widget.LinearLayoutManager.*;
import static com.udacity.android.popularmovie.PopularMovieCursorLoader.ID_DETAIL_LOADER;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_TITLE;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_VOTES;
import static com.udacity.android.popularmovie.utils.MovieUtils.EXTRA_MOVIE_DETAILS;
import static com.udacity.android.popularmovie.utils.MovieUtils.IMG_SIZE;
import static com.udacity.android.popularmovie.utils.MovieUtils.JSON_RESULTS;
import static com.udacity.android.popularmovie.utils.MovieUtils.MOVIE_DETAIL_REVIEWS;
import static com.udacity.android.popularmovie.utils.MovieUtils.MOVIE_DETAIL_TRAILERS;
import static com.udacity.android.popularmovie.utils.MovieUtils.PATH;

public class MovieDetailsActivity extends AppCompatActivity implements
        MovieTrailersAdapter.OnClickMovieTrailerHandler,
        PopularMovieCursorLoader.PopularMovieCursorLoaderInterface{

    private static final int INDEX_MOVIE_ID = 0;

    private MovieTrailersAdapter mTrailersAdapter;
    private MovieReviewsAdapter mReviewsAdapter;

    @BindView(R.id.tv_movie_title) TextView tv_movie_title;
    @BindView(R.id.tv_movie_release_date) TextView tv_movie_release_date;
    @BindView(R.id.tv_rating) TextView tv_rating;
    @BindView(R.id.rb_rating) RatingBar rbRating;
    @BindView(R.id.tv_overview) TextView tvOverview;
    @BindView(R.id.rc_trailers) RecyclerView rcTrailers;
    @BindView(R.id.rc_reviews) RecyclerView rcReviews;
    @BindView(R.id.fav_button) MaterialFavoriteButton favoriteButton;
    @BindView(R.id.img_poster_item_in_details) ImageView imgPosterImageView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private MovieData mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        mMovieData = getIntent().getParcelableExtra(EXTRA_MOVIE_DETAILS);
        tv_movie_title.setText(mMovieData.getOriginalTitle());
        tv_movie_release_date.setText(mMovieData.getReleaseDate());
        Picasso.with(this).load(PATH + IMG_SIZE + mMovieData.getPosterPath()).into(imgPosterImageView);
        tv_rating.setText(mMovieData.getVoteAverage().toString());
        rbRating.setRating(mMovieData.getVoteAverage().floatValue());
        tvOverview.setText(mMovieData.getOverview());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager horizontalLinearLayoutManager = new LinearLayoutManager(this, HORIZONTAL, false);
        rcTrailers.setLayoutManager(horizontalLinearLayoutManager);
        mTrailersAdapter = new MovieTrailersAdapter(this);
        rcTrailers.setAdapter(mTrailersAdapter);

        LinearLayoutManager verticalLinearLayoutManager = new LinearLayoutManager(this, VERTICAL, false);
        rcReviews.setLayoutManager(verticalLinearLayoutManager);
        mReviewsAdapter = new MovieReviewsAdapter();
        rcReviews.setAdapter(mReviewsAdapter);

        favoriteButton.setOnClickListener(v -> {
            Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(mMovieData.getId().toString()).build();
            if(!favoriteButton.isFavorite())
            {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_MOVIE_TITLE, mMovieData.getOriginalTitle());
                cv.put(COLUMN_MOVIE_POSTER_PATH, mMovieData.getPosterPath());
                cv.put(COLUMN_MOVIE_VOTES, mMovieData.getVoteAverage());
                cv.put(COLUMN_MOVIE_RELEASE, mMovieData.getReleaseDate());
                cv.put(COLUMN_MOVIE_ID, mMovieData.getId());
                cv.put(COLUMN_MOVIE_OVERVIEW, mMovieData.getOverview());
                getContentResolver().insert(uri, cv);
            }else{
                getContentResolver().delete(uri, null, null);
            }
            favoriteButton.toggleFavorite();

        });

        String ApiKey = getString(R.string.movie_db_key);
        String movieId = mMovieData.getId().toString();
        new MovieDetailsQueryTask(new FetchMovieTrailerTaskCompleteListener())
                .execute(ApiKey, MOVIE_DETAIL_TRAILERS, movieId);

        new MovieDetailsQueryTask(new FetchMovieReviewsTaskCompleteListener())
                .execute(ApiKey, MOVIE_DETAIL_REVIEWS, movieId);

        getSupportLoaderManager().restartLoader(ID_DETAIL_LOADER, null, new PopularMovieCursorLoader(this, this));
    }

    @Override
    public void onMovieTrailerClick(int position) {
        MovieTrailer movieTrailer = mTrailersAdapter.getMovieTrailersData(position);
        startTrailer(movieTrailer);
    }

    private void startTrailer(MovieTrailer movieTrailer) {
        Uri trailerUri = Uri.parse(NetworkUtils.getUrlMovieTrailerVideo(movieTrailer.getTrailerKey()));

        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        if(trailerIntent.resolveActivity(getPackageManager())!=null){
            startActivity(trailerIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFinished(Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            favoriteButton.setFavorite(false);
            return;
        }

        if(data.getInt(INDEX_MOVIE_ID) == mMovieData.getId())
            favoriteButton.setFavorite(true);

        data.close();

    }

    public class FetchMovieTrailerTaskCompleteListener implements AsyncTaskCompleteListener<String>
    {
        @Override
        public void onTaskComplete(String movieTrailersQueryResult)
        {
            if(movieTrailersQueryResult!=null)
                showMovieTrailers(movieTrailersQueryResult);
        }

        private void showMovieTrailers(String movieTrailersQueryResults) {
            ArrayList<MovieTrailer> movieTrailers = new ArrayList<>();
            try {
                JSONObject movieQueryJSON = new JSONObject(movieTrailersQueryResults);
                JSONArray movieJSONArray = movieQueryJSON.getJSONArray(JSON_RESULTS);
                for(int i =0; i<movieJSONArray.length(); i++){
                    JSONObject movieJSONObject = movieJSONArray.getJSONObject(i);
                    String ytKey = movieJSONObject.getString("key");
                    MovieTrailer movieTrailer = new MovieTrailer();
                    movieTrailer.setTrailerKey(ytKey);
                    movieTrailers.add(movieTrailer);
                }
                mTrailersAdapter.setMovieTrailersData(movieTrailers);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class FetchMovieReviewsTaskCompleteListener implements AsyncTaskCompleteListener<String>
    {
        @Override
        public void onTaskComplete(String movieReviewsQueryResult)
        {
            if(movieReviewsQueryResult!=null)
                showMovieData(movieReviewsQueryResult);
        }

        private void showMovieData(String movieDetailsQueryResults) {
            ArrayList<MovieReview> movieReviews = new ArrayList<>();
            try {
                JSONObject movieQueryJSON = new JSONObject(movieDetailsQueryResults);
                JSONArray movieJSONArray = movieQueryJSON.getJSONArray(JSON_RESULTS);
                for(int i =0; i<movieJSONArray.length(); i++){
                    JSONObject movieJSONObject = movieJSONArray.getJSONObject(i);
                    String author = movieJSONObject.getString("author");
                    String content = movieJSONObject.getString("content");
                    MovieReview review = new MovieReview();
                    review.setAuthor(author);
                    review.setContent(content);
                    movieReviews.add(review);
                }

                mReviewsAdapter.setReviewsData(movieReviews);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
