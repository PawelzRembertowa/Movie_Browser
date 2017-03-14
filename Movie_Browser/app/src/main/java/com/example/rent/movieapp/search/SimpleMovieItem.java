package com.example.rent.movieapp.search;

/**
 * Created by RENT on 2017-03-14.
 */

public class SimpleMovieItem {

    private String imdbID;
    private String poster;


    public SimpleMovieItem(String poster, String imdbID) {
        this.poster = poster;
        this.imdbID = imdbID;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getPoster() {
        return poster;
    }


}
