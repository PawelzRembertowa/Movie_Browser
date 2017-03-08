package com.example.rent.movieapp;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.GET;

/**
 * Created by RENT on 2017-03-08.
 */

public interface SearchService {
    @GET("/")
    Observable<SearchResult> search(@Field("s")String title);
}
