package com.udacity.android.popularmovie.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.R.attr.id;

public class MovieProvider extends ContentProvider{

    private MovieDbHelper movieDbHelper;
    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_ID = 101;
    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, CODE_MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_MOVIE_WITH_ID:
                String movieId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieId};
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("getType is not implemnted in PopularMovie");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        String movieId = uri.getLastPathSegment();
        long id;
        switch(sUriMatcher.match(uri)){
            case CODE_MOVIE_WITH_ID:
                id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new UnsupportedOperationException("Invalid uri " + uri);
        }

        if(id==-1)
            Log.v("FAV", "NOT INSERTED " + movieId);
        else
            Log.v("FAV", "INSERTED " + movieId);

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numberOfRows;

        if(selection == null) selection = "1";

        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE:
                numberOfRows = movieDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_MOVIE_WITH_ID:
                String movieId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieId};
                numberOfRows = movieDbHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments);

                break;
            default:
                throw new UnsupportedOperationException("Invalid uri" + uri);
        }
        Log.v("FAV", "DELETE " + numberOfRows);
        return numberOfRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Update is not implemnted in PopularMovie");
    }
}
