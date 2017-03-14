package com.example.rent.movieapp.search;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by RENT on 2017-03-08.
 */

public interface SearchService {



    @GET("/")

    Observable<SearchResult> search(@Query("page") int page, @Query("s") String title, @Query("y")String year,
                                    @Query("type")String type);
}
