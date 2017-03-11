package com.example.rent.movieapp.search;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.example.rent.movieapp.R;
import com.example.rent.movieapp.RetrofitProvider;
import com.example.rent.movieapp.listing.ListingActivity;
import com.example.rent.movieapp.listing.MovieListingItem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {

    private PosterRecyclerViewAdapter adapter;
    private Map<Integer, String> apiKeysMap = new HashMap<Integer, String>(){{
        put(R.id.radio_moviesID, "movie");
        put(R.id.radio_episodesID, "episode");
        put(R.id.radio_gamesID, "game");
        put(R.id.radio_serialsID, "serial");
    }};

    @BindView(R.id.numberPickerID)
    NumberPicker numberPicker;

    @BindView(R.id.textInputEditTextID)
    TextInputEditText editText;

    @BindView(R.id.search_buttonID)
    ImageView searchButton;

    @BindView(R.id.year_checkboxID)
    CheckBox yearCheckBox;

    @BindView(R.id.radio_groupID)
    RadioGroup radioGroup;

    @BindView(R.id.type_checkboxID)
    CheckBox typeCheckBox;

    @BindView(R.id.poster_headerID)
    RecyclerView posterHeaderRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        numberPicker.setMinValue(1950);
        numberPicker.setMaxValue(year);
        numberPicker.setValue(year);
        numberPicker.setWrapSelectorWheel(true);

        adapter = new PosterRecyclerViewAdapter();
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        posterHeaderRecyclerView.setLayoutManager(layoutManager);
        posterHeaderRecyclerView.setAdapter(adapter);
        posterHeaderRecyclerView.setHasFixedSize(true);
        posterHeaderRecyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        RetrofitProvider retrofitProvider = (RetrofitProvider) getApplication();
        Retrofit retrofit = retrofitProvider.provideRetrofit();
        SearchService searchService = retrofit.create(SearchService.class);
        searchService.search(1, "a*", "2016", null)
                .flatMap(searchResult -> Observable.fromIterable(searchResult.getItems()))
                .map(MovieListingItem::getPoster)
                .filter(posterUrl -> !"N/A".equalsIgnoreCase(posterUrl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(this::success ,this::error);
    }

    private void success(List<String> list){
        adapter.setUrls(list);
    }
    private void error (Throwable throwable) {

    }


    @OnCheckedChanged (R.id.type_checkboxID)
    void  onTypeCheckboxStateChanged(CompoundButton buttonView, boolean isChecked){
        radioGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }
    @OnCheckedChanged(R.id.year_checkboxID)
    void onYearCheckboxStateChanged(CompoundButton buttonView, boolean isChecked) {
        numberPicker.setVisibility(isChecked ? View.VISIBLE : View.GONE);

    }

    @OnClick(R.id.search_buttonID)
    void onSearchButtonClick(){
        int checkRadioId = radioGroup.getCheckedRadioButtonId();
        String typeKey = typeCheckBox.isChecked() ? apiKeysMap.get(checkRadioId) : null;
        int year = yearCheckBox.isChecked() ?  numberPicker.getValue() : ListingActivity.NO_YEAR_SELECTED;

        startActivity(ListingActivity.createIntent(SearchActivity.this, editText.getText().toString(),
                year, typeKey));
        }
    }


