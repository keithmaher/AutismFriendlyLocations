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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserLocationAdapterView extends RecyclerView.Adapter<UserLocationAdapterView.myViewHolder>{


    public List<Location> locationList;
    FragmentActivity activity;


    public UserLocationAdapterView(ArrayList<Location> locationList, FragmentActivity activity) {
        this.locationList = locationList;
        this.activity = activity;


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.locationrow, viewGroup, false);
        return new UserLocationAdapterView.myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int i) {

        final Location location = locationList.get(i);
        viewHolder.name.setText(location.getLocationName());
        viewHolder.address.setText(location.getLocationAddress());
        Picasso.get().load(location.locationIcon).fit().into(viewHolder.image);
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinydb = new TinyDB(activity);
                tinydb.putObject("concertObj", location);
                BaseFragment.userCommentsFragment(activity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView address;
        private ImageView image;
        private CardView card;

        public myViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.newsUserImg);
            name = itemView.findViewById(R.id.newsUserName);
            address = itemView.findViewById(R.id.newsUserDate);
            card = itemView.findViewById(R.id.locarionRowId);


        }
    }

}