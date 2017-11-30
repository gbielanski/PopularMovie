package com.udacity.android.popularmovie;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.udacity.android.popularmovie.data.MovieContract;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_TITLE;
import static com.udacity.android.popularmovie.data.MovieContract.MovieEntry.COLUMN_MOVIE_VOTES;


public class PopularMovieCursorLoader implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String[] MOVIE_DETAIL_PROJECTION = {COLUMN_MOVIE_ID};

    public static final String[] MOVIE_PROJECTION = {
            COLUMN_MOVIE_TITLE,
            COLUMN_MOVIE_POSTER_PATH,
            COLUMN_MOVIE_OVERVIEW,
            COLUMN_MOVIE_VOTES,
            COLUMN_MOVIE_RELEASE,
            COLUMN_MOVIE_ID,
    };

    private Context mContext;
    private PopularMovieCursorLoaderInterface mListener;
    public static final int ID_LOADER = 111;
    public static final int ID_DETAIL_LOADER = 222;

    interface PopularMovieCursorLoaderInterface{
        void onLoadFinished (Cursor data);
    }

    public PopularMovieCursorLoader(Context context, PopularMovieCursorLoaderInterface listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_DETAIL_LOADER:
                return new CursorLoader(mContext,
                        MovieContract.MovieEntry.CONTENT_URI,
                        MOVIE_DETAIL_PROJECTION,
                        null,
                        null,
                        null);
            case ID_LOADER:
                return new CursorLoader(mContext,
                        MovieContract.MovieEntry.CONTENT_URI,
                        MOVIE_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mListener.onLoadFinished(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
