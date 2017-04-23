package com.udacity.android.popularmovie;

import android.os.AsyncTask;

import java.io.IOException;

import static com.udacity.android.popularmovie.MovieUtils.MOVIE_DETAIL_REVIEWS;
import static com.udacity.android.popularmovie.MovieUtils.MOVIE_DETAIL_TRAILERS;

class MovieDetailsQueryTask extends AsyncTask<String, Void, String> {

    private AsyncTaskCompleteListener<String> listener;

    public MovieDetailsQueryTask(AsyncTaskCompleteListener<String> listener)
    {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String apiKey = params[0];
        String detailType = params[1];
        String movieId = params[2];
        String movieDetailsQueryResult = null;
        try {
            if (detailType.equals(MOVIE_DETAIL_REVIEWS))
                movieDetailsQueryResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getUrlMovieReviews(movieId, apiKey));
            else if (detailType.equals(MOVIE_DETAIL_TRAILERS))
                movieDetailsQueryResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getUrlMovieTrailers(movieId, apiKey));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieDetailsQueryResult;
    }

    @Override
    protected void onPostExecute(String detailsQueryResult)
    {
        super.onPostExecute(detailsQueryResult);
        listener.onTaskComplete(detailsQueryResult);
    }
}

