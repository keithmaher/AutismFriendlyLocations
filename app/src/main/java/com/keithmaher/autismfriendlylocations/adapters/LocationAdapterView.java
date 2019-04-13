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

public class LocationAdapterView extends RecyclerView.Adapter<LocationAdapterView.myViewHolder>{


    public List<Location> locationList;
    FragmentActivity activity;
    android.location.Location MylocationA;


    public LocationAdapterView(ArrayList<Location> locationList, FragmentActivity activity, android.location.Location MylocationA) {
        this.locationList = locationList;
        this.activity = activity;
        this.MylocationA = MylocationA;

    }

    public LocationAdapterView(ArrayList<Location> locationList, FragmentActivity activity) {
        this.locationList = locationList;
        this.activity = activity;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.locationrow, viewGroup, false);
        return new LocationAdapterView.myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int i) {

        final Location location = locationList.get(i);

        android.location.Location locationB = new android.location.Location("point B");

        locationB.setLatitude(location.getLocationLat());
        locationB.setLongitude(location.getLocationLat());

        String distance;

        if (MylocationA != null) {

            double distanceM = distanceBetween(MylocationA.getLatitude(), MylocationA.getLongitude(), location.getLocationLat(), location.getLocationLong());

            if (distanceM > 500.00){
                distanceM = distanceM/1000;
                distance = String.format("%.2f km", distanceM);
            }else {
                distance = String.format("%.2f m", distanceM);
            }
        }else{
            distance = "Calculating...";
        }

        viewHolder.name.setText(location.getLocationName());
        viewHolder.address.setText(location.getLocationAddress());
        viewHolder.distance.setText(distance);
        Picasso.get().load(location.locationIcon).fit().into(viewHolder.image);
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinydb = new TinyDB(activity);
                tinydb.putObject("locationObj", location);
                BaseFragment.singleLocationFragment(activity);
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
        private TextView distance;
        private CardView card;

        public myViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.newsUserImg);
            name = itemView.findViewById(R.id.newsUserName);
            address = itemView.findViewById(R.id.newsUserDate);
            card = itemView.findViewById(R.id.locarionRowId);
            distance = itemView.findViewById(R.id.rowDistance);


        }
    }

    private double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = dist * 180.0 / Math.PI;
        dist = dist * 60 * 1.1515*1000;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

}