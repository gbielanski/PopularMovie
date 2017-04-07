package com.udacity.android.popularmovie;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static com.udacity.android.popularmovie.MovieUtils.API_KEY;
import static com.udacity.android.popularmovie.MovieUtils.THE_HIGHEST_RATED_URL_STRING;
import static com.udacity.android.popularmovie.MovieUtils.THE_MOST_POPULAR_URL_STRING;


class NetworkUtils {
    static URL getUrlTheMostPopular(String api_key){
        URL url = null;
        Uri uri = Uri.parse(THE_MOST_POPULAR_URL_STRING).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    static URL getUrlHighestRated(String api_key){
        URL url = null;
        Uri uri = Uri.parse(THE_HIGHEST_RATED_URL_STRING).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /*
    *  Function copied from Udacity course
    * */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
