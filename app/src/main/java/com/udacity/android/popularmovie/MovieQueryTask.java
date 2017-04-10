package com.udacity.android.popularmovie;

import android.os.AsyncTask;

import java.io.IOException;

import static com.udacity.android.popularmovie.MovieUtils.SORT_TYPE_RATE;

class MovieQueryTask extends AsyncTask<String, Void, String> {

    private AsyncTaskCompleteListener<String> listener;

    public MovieQueryTask(AsyncTaskCompleteListener<String> listener)
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
    protected void onPostExecute(String queryResult)
    {
        super.onPostExecute(queryResult);
        listener.onTaskComplete(queryResult);
    }
}

