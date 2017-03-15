package com.example.rent.movieapp.listing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RENT on 2017-03-15.
 */

public class ResultAggregator {

    private String response;
    private List<MovieListingItem> movieItems = new ArrayList<>();
    private int totalItemResult;
    private int totalItemsResults;

    public void setTotalItemResult (int totalItemResult) {
        this.totalItemResult=totalItemResult;
    }

    public void  addNewItems (List<MovieListingItem> newItems) {
        movieItems.addAll(newItems);
    }

    public List<MovieListingItem> getMovieItems() {
        return movieItems;
    }

    public int getTotalItemResult() {
        return totalItemResult;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getTotalItemsResults() {
        return totalItemsResults;
    }
}
