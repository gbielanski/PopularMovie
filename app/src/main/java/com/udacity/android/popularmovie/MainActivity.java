package com.udacity.android.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.android.popularmovie.adapter.MoviePosterAdapter;
import com.udacity.android.popularmovie.data.MovieContract;
import com.udacity.android.popularmovie.data.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_TITLE;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_VOTES;
import static com.udacity.android.popularmovie.utils.MovieUtils.EXTRA_MOVIE_DETAILS;
import static com.udacity.android.popularmovie.utils.MovieUtils.JSON_MOVIE_ID;
import static com.udacity.android.popularmovie.utils.MovieUtils.JSON_ORIGINAL_TITLE;
import static com.udacity.android.popularmovie.utils.MovieUtils.JSON_OVERVIEW;
import static com.udacity.android.popularmovie.utils.MovieUtils.JSON_POSTER_PATH;
import static com.udacity.android.popularmovie.utils.MovieUtils.JSON_RELEASE_DATE;
import static com.udacity.android.popularmovie.utils.MovieUtils.JSON_RESULTS;
import static com.udacity.android.popularmovie.utils.MovieUtils.JSON_VOTE_AVERAGE;
import static com.udacity.android.popularmovie.utils.MovieUtils.SORT_TYPE_FAVORITE;
import static com.udacity.android.popularmovie.utils.MovieUtils.SORT_TYPE_POPULAR;
import static com.udacity.android.popularmovie.utils.MovieUtils.SORT_TYPE_RATE;

public class MainActivity extends AppCompatActivity implements MoviePosterAdapter.OnClickMoviePosterHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_LOADER = 111;
    public static final String[] MOVIE_DETAIL_PROJECTION = {
            COLUMN_MOVIE_TITLE,
            COLUMN_MOVIE_POSTER_PATH,
            COLUMN_MOVIE_OVERVIEW,
            COLUMN_MOVIE_VOTES,
            COLUMN_MOVIE_RELEASE,
            COLUMN_MOVIE_ID,
    };
    private static final int INDEX_TITLE = 0;
    private static final int INDEX_POSTER_PATH = 1;
    private static final int INDEX_OVERVIEW = 2;
    private static final int INDEX_VOTES = 3;
    private static final int INDEX_RELEASE = 4;
    private static final int INDEX_MOVIE_ID = 5;

	private String mFetchDataType = SORT_TYPE_POPULAR;
	private String FETCH_DATA_TYPE = "FETCH_DATA_TYPE";

    private MoviePosterAdapter mAdapter;
    @BindView(R.id.tv_error_message) TextView mErrorMessageTextView;
    @BindView(R.id.pb_loading_progress) ProgressBar mProgressBar;
    @BindView(R.id.rc_movie_grid) RecyclerView mRecyclerView;
    @BindView(R.id.my_toolbar) Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MoviePosterAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchMovieData(String sortType) {
		mFetchDataType = sortType;
        if(sortType.equals(SORT_TYPE_POPULAR) || sortType.equals(SORT_TYPE_RATE)) {
            if (isOnline()) {
                mProgressBar.setVisibility(View.VISIBLE);
                new MovieQueryTask(new FetchMovieDataTaskCompleteListener())
                        .execute(getString(R.string.movie_db_key), sortType);
            } else
                showErrorMessage(getString(R.string.error_string_connection));
        }else{
            mProgressBar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(ID_LOADER, null, this);
        }
    }


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(FETCH_DATA_TYPE, mFetchDataType);
		Log.v("HOP", "HOP " + mFetchDataType);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		mFetchDataType = savedInstanceState.getString(FETCH_DATA_TYPE);
		Log.v("HOP", "HOP " + mFetchDataType);

	}

	@Override
	protected void onResume() {
		super.onResume();
		fetchMovieData(mFetchDataType);
	}

	private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null)
            return false;

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
    public void moviePosterOnClick(int position, View posterView) {
        Class movieDetailedClass = MovieDetailsActivity.class;
        Intent intent = new Intent(this, movieDetailedClass);
        intent.putExtra(EXTRA_MOVIE_DETAILS, mAdapter.getMovieData().get(position));

        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    posterView,
                    posterView.getTransitionName()).toBundle();
        }

        startActivity(intent, bundle);
    }

    private void showErrorMessage(String errorMsg){
        mErrorMessageTextView.setText(errorMsg);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_LOADER:
                return new CursorLoader(this,
                        MovieContract.MovieEntry.CONTENT_URI,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);

        ArrayList<MovieData> movieDataArray = new ArrayList<>();
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
			mAdapter.addMovieData(movieDataArray);
			return;
		}

        do{
            MovieData movie = new MovieData();

            movie.setOriginalTitle(data.getString(INDEX_TITLE));
            movie.setPosterPath(data.getString(INDEX_POSTER_PATH));
            movie.setOverview(data.getString(INDEX_OVERVIEW));
            movie.setVoteAverage(data.getDouble(INDEX_VOTES));
            movie.setReleaseDate(data.getString(INDEX_RELEASE));
            movie.setId(data.getInt(INDEX_MOVIE_ID));

            movieDataArray.add(movie);
        }while (data.moveToNext());
        mAdapter.addMovieData(movieDataArray);
        data.close();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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
                        movieData.setId(movieJSONObject.getInt(JSON_MOVIE_ID));
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
        }else if (item.getItemId() == R.id.favorite) {
            fetchMovieData(SORT_TYPE_FAVORITE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
