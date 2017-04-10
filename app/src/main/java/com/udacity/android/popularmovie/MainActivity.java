package com.udacity.android.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.udacity.android.popularmovie.MovieUtils.EXTRA_MOVIE_DETAILS;
import static com.udacity.android.popularmovie.MovieUtils.JSON_ORIGINAL_TITLE;
import static com.udacity.android.popularmovie.MovieUtils.JSON_OVERVIEW;
import static com.udacity.android.popularmovie.MovieUtils.JSON_POSTER_PATH;
import static com.udacity.android.popularmovie.MovieUtils.JSON_RELEASE_DATE;
import static com.udacity.android.popularmovie.MovieUtils.JSON_RESULTS;
import static com.udacity.android.popularmovie.MovieUtils.JSON_VOTE_AVERAGE;
import static com.udacity.android.popularmovie.MovieUtils.SORT_TYPE_POPULAR;
import static com.udacity.android.popularmovie.MovieUtils.SORT_TYPE_RATE;

public class MainActivity extends AppCompatActivity implements MoviePosterAdapter.OnClickMoviePosterHandler {

    private MoviePosterAdapter mAdapter;
    private TextView mErrorMessageTextView;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rc_movie_grid);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_progress);
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MoviePosterAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        fetchMovieData(SORT_TYPE_POPULAR);
    }

    private void fetchMovieData(String sortType) {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            new MovieQueryTask(new FetchMovieDataTaskCompleteListener())
                    .execute(getString(R.string.movie_db_key), sortType);
        } else
            showErrorMessage(getString(R.string.error_string_connection));
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 342;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public void moviePosterOnClick(int position) {
        Class movieDetailedClass = MovieDetailsActivity.class;
        Intent intent = new Intent(this, movieDetailedClass);
        intent.putExtra(EXTRA_MOVIE_DETAILS, mAdapter.mMovieData.get(position));
        startActivity(intent);
    }

    private void showErrorMessage(String errorMsg){
        mErrorMessageTextView.setText(errorMsg);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

    }

    public class FetchMovieDataTaskCompleteListener implements AsyncTaskCompleteListener<String>
    {
        @Override
        public void onTaskComplete(String movieQueryResult)
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(movieQueryResult!=null)
                showMovieData(movieQueryResult);
            else
                showErrorMessage(getString(R.string.error_string));
        }

        private void showMovieData(String movieQueryResult) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mErrorMessageTextView.setVisibility(View.INVISIBLE);
            ArrayList<MovieData> movieDataArray = new ArrayList<>();
            try {
                JSONObject movieQueryJSON = new JSONObject(movieQueryResult);
                JSONArray movieJSONArray = movieQueryJSON.getJSONArray(JSON_RESULTS);
                for(int i =0; i<movieJSONArray.length(); i++){
                    JSONObject movieJSONObject = movieJSONArray.getJSONObject(i);
                    MovieData movieData = new MovieData();
                    movieData.setOriginalTitle(movieJSONObject.getString(JSON_ORIGINAL_TITLE));
                    movieData.setPosterPath(movieJSONObject.getString(JSON_POSTER_PATH));
                    movieData.setOverview(movieJSONObject.getString(JSON_OVERVIEW));
                    movieData.setVoteAverage(movieJSONObject.getDouble(JSON_VOTE_AVERAGE));
                    movieData.setReleaseDate(movieJSONObject.getString(JSON_RELEASE_DATE));
                    movieDataArray.add(movieData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(movieDataArray.size() > 0)
                mAdapter.addMovieData(movieDataArray);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.most_popular) {
            fetchMovieData(SORT_TYPE_POPULAR);
            return true;
        } else if (item.getItemId() == R.id.highest_rate) {
            fetchMovieData(SORT_TYPE_RATE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
