package com.keithmaher.autismfriendlylocations.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.Utils.TinyDB;
import com.keithmaher.autismfriendlylocations.fragments.BaseFragment;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.keithmaher.autismfriendlylocations.models.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapterView extends RecyclerView.Adapter<NewsAdapterView.myViewHolder>{


    public List<News> locationList;
    FragmentActivity activity;


    public NewsAdapterView(ArrayList<News> locationList, FragmentActivity activity) {
        this.locationList = locationList;
        this.activity = activity;


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsrow, viewGroup, false);
        return new NewsAdapterView.myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int i) {

        final News news = locationList.get(i);
        viewHolder.name.setText(news.getNewsName());
        viewHolder.date.setText("Date added: "+news.getNewsDate());
        viewHolder.location.setText("Commented on "+news.getNewsLocation());
        Picasso.get().load(news.getNewsImg()).fit().into(viewHolder.image);

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView location;
        private ImageView image;
        private CardView card;

        public myViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.newsIcon);
            name = itemView.findViewById(R.id.newsUserName);
            date = itemView.findViewById(R.id.newsUserDate);
            card = itemView.findViewById(R.id.cardId);
            location = itemView.findViewById(R.id.newsLocation);


        }
    }

}