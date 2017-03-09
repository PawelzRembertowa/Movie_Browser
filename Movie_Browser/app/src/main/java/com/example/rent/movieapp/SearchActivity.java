package com.example.rent.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

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


