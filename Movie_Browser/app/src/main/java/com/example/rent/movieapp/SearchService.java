package com.example.rent.movieapp;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by RENT on 2017-03-08.
 */

public interface SearchService {
    @GET("/")

    Observable<SearchResult> search(@Query("s") String title);
}
