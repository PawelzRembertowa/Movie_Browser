package com.example.rent.movieapp.listing;

import com.example.rent.movieapp.search.SearchResult;
import com.example.rent.movieapp.search.SearchService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import nucleus.presenter.Presenter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by RENT on 2017-03-07.
 */

public class ListingPresenter extends Presenter<ListingActivity> implements OnLoadNextPageListener {

    private ResultAggregator resultAggregator = new ResultAggregator();

    private Retrofit retrofit;
    private String title;
    private String stringYear;
    private String type;
    private boolean isLoadingFromStart;

//    public Observable<ResultAggregator> subscribeOnLoadingResult () {
//
//    }

    public void startLoadingItems(String title, int year, String type) {
        this.title = title;
        this.type = type;
        stringYear = year == ListingActivity.NO_YEAR_SELECTED ? null : String.valueOf(year);

        if (resultAggregator.getMovieItems().size() == 0) {
            loadNextPage(1);
            isLoadingFromStart = true;
        }
    }

    @Override
    protected void onTakeView(ListingActivity listingActivity) {
        super.onTakeView(listingActivity);
        if (!isLoadingFromStart) {
            listingActivity.setNewAggregatorResult(resultAggregator);
        }
    }
    public void setRetrofit (Retrofit retrofit){
        this.retrofit=retrofit;
    }



    @Override
    public void loadNextPage(int page) {
        retrofit.create(SearchService.class).search(page,title,
                stringYear,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResult -> {
                    resultAggregator.addNewItems(searchResult.getItems());
                    resultAggregator.setTotalItemResult(Integer.parseInt(searchResult.getTotalResults()));
                    resultAggregator.setResponse(searchResult.getResponse());
                    getView().setNewAggregatorResult(resultAggregator);
                }, throwable -> {
                });


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
