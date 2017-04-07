package com.udacity.android.popularmovie;


class MovieUtils {
    static final String SORT_TYPE_RATE = "RATE";
    static final String SORT_TYPE_POPULAR = "POPULAR";
    static final String EXTRA_MOVIE_DETAILS = "MOVIE_DETAILS";
    static final String THE_MOST_POPULAR_URL_STRING =
            "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
    static final String THE_HIGHEST_RATED_URL_STRING =
            "https://api.themoviedb.org/3/discover/movie?certification_country=US&certification=R&sort_by=vote_average.desc";
    static final String API_KEY = "api_key";
    static final String PATH = "http://image.tmdb.org/t/p/";
    static final String IMG_SIZE = "w342";
    static final String JSON_RESULTS = "results";
    static final String JSON_ORIGINAL_TITLE = "original_title";
    static final String JSON_POSTER_PATH = "poster_path";
    static final String JSON_OVERVIEW = "overview";
    static final String JSON_VOTE_AVERAGE = "vote_average";
    static final String JSON_RELEASE_DATE = "release_date";

}
