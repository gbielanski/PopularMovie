package com.udacity.android.popularmovie.data;

/**
 * Created by Grzegorz on 22.04.2017.
 */

public class MovieTrailer {
    private String trailerKey;
    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    @Override
    public String toString() {
        return "trailerKey : " + trailerKey;
    }
}
