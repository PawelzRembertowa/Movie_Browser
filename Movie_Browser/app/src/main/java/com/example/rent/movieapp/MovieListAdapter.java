package com.example.rent.movieapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

/**
 * Created by RENT on 2017-03-08.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {

    private List<MovieListingItem> items = Collections.emptyList();


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(layout);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MovieListingItem movieListingItem = items.get(position);
        Glide.with(holder.poster.getContext()).load(movieListingItem.getPoster()).into(holder.poster);
        holder.titleAndYear.setText(movieListingItem.getTitle() + " (" + movieListingItem.getYear() + ")");
        holder.type.setText("type: " + movieListingItem.getType());
    }

    public void setItems(List<MovieListingItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    //TODO viewHolder sluzy do wyszukiwania widokow, jak findbyviewID, tylko ze jest wydajniejsze
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView titleAndYear;
        TextView type;
    public MyViewHolder(View itemView) {
        super(itemView);
        poster = (ImageView) itemView.findViewById(R.id.posterID);
        titleAndYear = (TextView) itemView.findViewById(R.id.title_and_year_ID);
        type = (TextView) itemView.findViewById(R.id.typeID);
    }
}
}
