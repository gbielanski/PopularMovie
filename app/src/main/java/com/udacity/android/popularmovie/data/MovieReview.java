package com.udacity.android.popularmovie.data;

/**
 * Created by Grzegorz on 22.04.2017.
 */

public class MovieReview {
    String author;
    String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "author : " + author + " content : " + getContent();
    }
}
