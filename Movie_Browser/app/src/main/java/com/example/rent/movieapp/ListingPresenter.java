package com.example.rent.movieapp;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.Observable;
import nucleus.presenter.Presenter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RENT on 2017-03-07.
 */

public class ListingPresenter extends Presenter<ListingActivity> {

    private Retrofit retrofit;

    //TODO dodawanie bibliotek RxJava? (jest nowsze, wiec to co jest na dole zostalo zakomentowane
    public ListingPresenter(){
      retrofit = new Retrofit.Builder()
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              .addConverterFactory(GsonConverterFactory.create())
              .baseUrl("https://www.omdbapi.com")
              .build();
    }

    public Observable<SearchResult> getDataAsync (String title) {
        return retrofit.create(SearchService.class).search(title);


    }

//    //TODO metoda do pobierania danych z adresu URL
//    public String getData(String title) throws IOException {
//        String stringUrl = "https://www.omdbapi.com/?s" + title;
//        URL url = new URL(stringUrl);
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        //TODO ustawiamy czas na ,,Timeout''
//        urlConnection.setConnectTimeout(3000);
//        InputStream inputStream = urlConnection.getInputStream();
//        return convertStreamToString(inputStream);
//    }
//
//    private String convertStreamToString(java.io.InputStream is) {
//    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
//        return s.hasNext() ? s.next() : "";
//    }

}
