package com.udacity.android.popularmovie;

/**
 * Created by Grzegorz on 22.04.2017.
 */

class MovieTrailer {
    String youtubeKey;
    public String getYoutubeKey() {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    @Override
    public String toString() {
        return "youtubeKey : " + youtubeKey;
    }
}
