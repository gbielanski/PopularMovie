package com.udacity.android.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Grzegorz on 05.04.2017.
 */
class MovieData implements Parcelable{
    private String original_title;
    private String poster_path;
    private String overview;
    private Double vote_average;
    private String release_date;

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    protected MovieData(Parcel in) {
        original_title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        vote_average = in.readDouble();
        release_date = in.readString();

    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    public MovieData() {

    }

    @Override
    public String toString() {
        return "original_title : " + original_title +
                ", release_date : " + release_date +
                " vote_average :" + vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeDouble(vote_average);
        dest.writeString(release_date);
    }
}
