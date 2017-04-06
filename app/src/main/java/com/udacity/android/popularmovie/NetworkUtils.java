package com.udacity.android.popularmovie;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Grzegorz on 05.04.2017.
 */

public class NetworkUtils {

    private static final String THE_MOST_POPULAR_URL_STRING =
            "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
    public static final String THE_HEIGHEST_RATED_URL_STRING =
            "https://api.themoviedb.org/3/discover/movie?certification_country=US&certification=R&sort_by=vote_average.desc";

    public static URL getUrlTheMostPopular(String api_key){
        URL url = null;
        Uri uri = Uri.parse(THE_MOST_POPULAR_URL_STRING).buildUpon().appendQueryParameter("api_key", api_key).build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL getUrlHighestRated(String api_key){
        URL url = null;

        Uri uri = Uri.parse(THE_HEIGHEST_RATED_URL_STRING).buildUpon().appendQueryParameter("api_key", api_key).build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
