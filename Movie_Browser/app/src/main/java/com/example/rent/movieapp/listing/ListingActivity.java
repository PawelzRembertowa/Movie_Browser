package com.example.rent.movieapp.listing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.rent.movieapp.R;
import com.example.rent.movieapp.RetrofitProvider;
import com.example.rent.movieapp.search.SearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(ListingPresenter.class)
public class ListingActivity extends NucleusAppCompatActivity<ListingPresenter> implements CurrentItemListener, ShowOrHideCounter{

    private static final String SEARCH_TITLE = "search title";
    private static final String SEARCH_YEAR = "search year";
    public static final int NO_YEAR_SELECTED = -1;
    private static final String SEARCH_TYPE = "search type";
    private MovieListAdapter adapter;

    @BindView(R.id.viewFlipperID)
    ViewFlipper viewFlipper;

    @BindView(R.id.noInternetImageViewID)
    ImageView noInternetImage;

    @BindView(R.id.recycler_viewID)
    RecyclerView recyclerView;

    @BindView(R.id.no_resultsID)
    FrameLayout noResults;
    private EndlessScrollListener endlessScrollListener;

    @BindView(R.id.counter_ID)
    TextView counter;

    @BindView(R.id.swipe_refreshID)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        if(savedInstanceState==null) {
            RetrofitProvider retrofitProvider = (RetrofitProvider) getApplication();
            getPresenter().setRetrofit(retrofitProvider.provideRetrofit());
        }

        String title = getIntent().getStringExtra(SEARCH_TITLE);
        String type = getIntent().getStringExtra(SEARCH_TYPE);
        int year = getIntent().getIntExtra(SEARCH_YEAR, NO_YEAR_SELECTED);

        ButterKnife.bind(this);
        adapter = new MovieListAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        endlessScrollListener = new EndlessScrollListener(layoutManager, getPresenter());
        recyclerView.addOnScrollListener(endlessScrollListener);
        endlessScrollListener.setCurrentItemListener(this);
        endlessScrollListener.setShowOrHideCounter(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoading(title, year, type);
            }
        });

        startLoading(title, year, type);

    }

    private void startLoading(String title, int year, String type) {
        getPresenter().getDataAsync(title, year, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::success, this::error);
    }

    @OnClick(R.id.noInternetImageViewID)
    public void onNoInternetViewClick(View view){
    Toast.makeText(this, "Kliknalem no internet image view", Toast.LENGTH_LONG).show();
    }


    private void error(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noInternetImage));
    }

    public void appendItems (SearchResult searchResult) {
        adapter.addItems(searchResult.getItems());
        endlessScrollListener.setTotalItemsNumber(Integer.parseInt(searchResult.getTotalResults()));
    }


    private void success(SearchResult searchResult) {
        swipeRefreshLayout.setRefreshing(false);
        if ("False".equalsIgnoreCase(searchResult.getResponse())) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(noResults));
        } else {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(swipeRefreshLayout));
            adapter.setItems(searchResult.getItems());
            endlessScrollListener.setTotalItemsNumber(Integer.parseInt(searchResult.getTotalResults()));
        }
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
    public static Intent createIntent(Context context, String title, int year, String type){
        Intent intent = new Intent(context, ListingActivity.class);
        intent.putExtra(SEARCH_TITLE, title);
        intent.putExtra(SEARCH_YEAR, year);
        intent.putExtra(SEARCH_TYPE, type);
        return intent;
    }

    @Override
    public void onNewCurrentItem(int currentItem, int totalItemsCount) {
        counter.setText(currentItem + "/" + totalItemsCount);
        counter.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCounter() {
        counter.setVisibility(View.GONE);
        counter.animate().translationX(0).start();
    }

    @Override
    public void hideCounter() {
        counter.animate().translationX(counter.getWidth()*2).start();
    }
}
