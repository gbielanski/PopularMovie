package com.udacity.android.popularmovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
        int spanCount = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MoviePosterAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        new MovieQueryTask().execute(getString(R.string.movie_db_key), SORT_TYPE_POPULAR);
    }

    @Override
    public void moviePosterOnClick(int position) {
        Class movieDetailedClass = MovieDetailsActivity.class;
        Intent intent = new Intent(this, movieDetailedClass);
        intent.putExtra(EXTRA_MOVIE_DETAILS, mAdapter.mMovieData.get(position));
        startActivity(intent);
    }

    private void showErrorMessage(){
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

    }

    private class MovieQueryTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String apiKey = params[0];
            String sortType = params[1];
            String movieQueryResult = null;
            try {
                if (sortType.equals(SORT_TYPE_RATE))
                    movieQueryResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getUrlHighestRated(apiKey));
                else
                    movieQueryResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getUrlTheMostPopular(apiKey));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieQueryResult;
        }

        @Override
        protected void onPostExecute(String movieQueryResult) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(movieQueryResult!=null)
                showMovieData(movieQueryResult);
            else
                showErrorMessage();
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
        if(item.getItemId() == R.id.most_popular){
            new MovieQueryTask().execute(getString(R.string.movie_db_key), SORT_TYPE_POPULAR);
            return true;
        }else if (item.getItemId() == R.id.highest_rate){
            new MovieQueryTask().execute(getString(R.string.movie_db_key), SORT_TYPE_RATE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
