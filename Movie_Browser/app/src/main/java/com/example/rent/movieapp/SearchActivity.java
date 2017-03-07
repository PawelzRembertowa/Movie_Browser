package com.example.rent.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SearchActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TextInputEditText editText = (TextInputEditText) findViewById(R.id.textInputEditTextID);

        //TODO klikanie w przycisk statyczna metoda
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_buttonID);
        searchButton.setOnClickListener(v ->
                startActivity(ListingActivity.createIntent(SearchActivity.this, editText.getText().toString())));

    }

}
