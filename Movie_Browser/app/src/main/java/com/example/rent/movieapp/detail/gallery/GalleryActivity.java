package com.example.rent.movieapp.detail.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rent.movieapp.R;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity {
    private static final String URL_KEY = "url_key";

    @BindView(R.id.full_screen_imageID)
    ImageView fullScreenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        //TODO metoda stopujaca animacje
        //postponeEnterTransition();
        //TODO kiedy juz wiemy ze obrazek sie zaladowal i animacja startuje
        //startPostponedEnterTransition();

        String url = getIntent().getStringExtra(URL_KEY);
        Glide.with(this).load(url).into(fullScreenImage);
    }

    public static void startActivity (Activity activity, String url, ImageView imageView) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, imageView, "profile");

        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra(URL_KEY, url);
        activity.startActivity(intent, options.toBundle());
    }

}
