package com.udacity.android.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

class MovieData implements Parcelable{
    private String originalTitle;
    private String posterPath;
    private String overview;
    private Double voteAverage;
    private String releaseDate;

    String getOriginalTitle() {
        return originalTitle;
    }

    void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    String getPosterPath() {
        return posterPath;
    }

    void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    String getOverview() {
        return overview;
    }

    void setOverview(String overview) {
        this.overview = overview;
    }

    Double getVoteAverage() {
        return voteAverage;
    }

    void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    private MovieData(Parcel in) {
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
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

    MovieData() {}

    @Override
    public String toString() {
        return "originalTitle : " + originalTitle +
                ", releaseDate : " + releaseDate +
                " voteAverage :" + voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
    }
}
