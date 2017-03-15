package com.example.rent.movieapp.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rent.movieapp.R;
import com.example.rent.movieapp.RetrofitProvider;
import com.example.rent.movieapp.detail.gallery.GalleryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(DetailPresenter.class)
public class DetailActivity extends NucleusAppCompatActivity<DetailPresenter> {

    private static final String ID_KEY = "id_key";
    private Disposable subscribe;

    @BindView(R.id.posterID)
    ImageView poster;

    @BindView(R.id.plotID)
    TextView plot;

    @BindView(R.id.directorID)
    TextView director;

    @BindView(R.id.actorsID)
    TextView actors;

    @BindView(R.id.runtimeID)
    TextView runtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        String imdbID = getIntent().getStringExtra(ID_KEY);
        RetrofitProvider retrofitProvider = (RetrofitProvider) getApplication();
        getPresenter().setRetrofit(retrofitProvider.provideRetrofit());

        subscribe = getPresenter().loadDetail(imdbID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::success, this::error);


    }


    //TODO takie cos robimy by uniknac wycieku pamieci (np. w momencie kiedy watek jest w trakcie, a my przewracamy ekranem non-stop
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (subscribe != null){
            subscribe.dispose();
        }
    }

    private void success(MovieItem movieItem){
        Glide.with(this).load(movieItem.getPoster()).into(poster);

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryActivity.startActivity(DetailActivity.this, movieItem.getPoster(),poster);
            }
        });

//        plot.setText(movieItem.getPlot());
//        director.setText(movieItem.getDirector());
//        actors.setText(movieItem.getActors());
//        runtime.setText(movieItem.getRuntime());

    }

    private void error(Throwable throwable) {

    }


    public static Intent createIntent (Context context, String imdbID) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ID_KEY, imdbID);
        return intent;
    }
}
