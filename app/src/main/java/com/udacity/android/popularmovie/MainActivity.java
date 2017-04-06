package com.udacity.android.popularmovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviePosterAdapter.OnClickMoviePosterHandler {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageTextView;
    private ProgressBar mProgresBar;
    private MoviePosterAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.rc_movie_grid);
        mErrorMessageTextView = (TextView)findViewById(R.id.tv_error_message);
        mProgresBar = (ProgressBar)findViewById(R.id.pb_loading_progress);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MoviePosterAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        new MovieQueryTask().execute(getString(R.string.movie_db_key));
        Log.v("movieQueryString", NetworkUtils.getUrlTheMostPopular(getString(R.string.movie_db_key)).toString());
    }

    @Override
    public void moviePosterOnClick(int position) {
        Class movieDetailedClass = MovieDetailsActivity.class;
        Intent intent = new Intent(this, movieDetailedClass);
        intent.putExtra("MOVIE_DETAILS", mAdapter.mMovieData.get(position));
        startActivity(intent);
//        Toast.makeText(this, "New activity will be started", Toast.LENGTH_LONG).show();
    }

    public class MovieQueryTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String apiKey = params[0];
            String movieQueryResult = null;
            try {
                movieQueryResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getUrlTheMostPopular(apiKey));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return movieQueryResult;
        }

        @Override
        protected void onPostExecute(String movieQueryResult) {

            ArrayList<MovieData> movieDataArray = new ArrayList<>();
            try {
                JSONObject movieQueryJSON = new JSONObject(movieQueryResult);
                JSONArray movieJSONArray = movieQueryJSON.getJSONArray("results");

                for(int i =0; i<movieJSONArray.length(); i++){
                    JSONObject movieJSONObject = movieJSONArray.getJSONObject(i);

                    MovieData movieData = new MovieData();
                    movieData.setOriginal_title(movieJSONObject.getString("original_title"));
                    movieData.setPoster_path(movieJSONObject.getString("poster_path"));
                    movieData.setOverview(movieJSONObject.getString("overview"));
                    movieData.setVote_average(movieJSONObject.getDouble("vote_average"));
                    movieData.setRelease_date(movieJSONObject.getString("release_date"));
                    movieDataArray.add(movieData);
                    Log.v("MovieData", movieData.toString());

                }
                Log.v("JSONArray", "" + movieJSONArray.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(movieDataArray.size() > 0)
                mAdapter.addMovieData(movieDataArray);

        }
    }
}
