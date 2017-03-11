package com.example.rent.movieapp.search;

import com.example.rent.movieapp.listing.MovieListingItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by RENT on 2017-03-07.
 */

public class SearchResult {

    @SerializedName("Search")
    private List<MovieListingItem>items;
    private String totalResults;
    @SerializedName("Response")
    private String response;

    public List<MovieListingItem> getItems() {
        return items;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setItems(List<MovieListingItem> items) {
        this.items = items;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
