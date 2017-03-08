package com.example.rent.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> {

    private static final String SEARCH_TITLE = "search title";
    private MovieListAdapter adapter;
    private ViewFlipper viewFlipper;
    private ImageView noInternetImage;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        String title = getIntent().getStringExtra(SEARCH_TITLE);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_viewID);
        adapter = new MovieListAdapter();
        recyclerView.setAdapter(adapter);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipperID);
        noInternetImage = (ImageView) findViewById(R.id.noInternetImageViewID);

        getPresenter().getDataAsync(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::success, this::error);

    }

    private void error(Throwable throwable) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
    }


    private void success(SearchResult searchResult) {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
        adapter.setItems(searchResult.getItems());
    }


    public static Intent createIntent(Context context, String title){
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        return intent;
    }

    public void setDataOnUiThread(SearchResult result, boolean isProblemWithInternetConnection) {
        //TODO powrot do watku glownego
        runOnUiThread(() -> {
            if (isProblemWithInternetConnection) {

                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
            } else {
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
                adapter.setItems(result.getItems());
            }
        });
    }

}
