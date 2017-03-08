package com.example.rent.movieapp;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import nucleus.presenter.Presenter;

/**
 * Created by RENT on 2017-03-07.
 */

public class ListingPresenter extends Presenter<ListingActivity> {

    public void getDataAsync (String title) {
        new Thread() {
            @Override
            public void run () {
                try {
                    String result = getData(title);
                    //TODO przerabia info z Gsona na przyjazny format dla androida
                    SearchResult searchResult = new Gson().fromJson(result, SearchResult.class);
                    getView().setDataOnUiThread(searchResult, false);
                } catch (IOException e) {
                    getView().setDataOnUiThread(null, true);
                }
            }
        }.start();
    }

    //TODO metoda do pobierania danych z adresu URL
    public String getData(String title) throws IOException {
        String stringUrl = "https://www.omdbapi.com/?s" + title;
        URL url = new URL(stringUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //TODO ustawiamy czas na ,,Timeout''
        urlConnection.setConnectTimeout(5000);
        InputStream inputStream = urlConnection.getInputStream();
        return convertStreamToString(inputStream);
    }

    private String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
