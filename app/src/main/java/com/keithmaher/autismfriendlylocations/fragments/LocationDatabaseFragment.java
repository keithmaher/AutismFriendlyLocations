package com.keithmaher.autismfriendlylocations.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.LocationAdapterView;
import com.keithmaher.autismfriendlylocations.models.Location;

import pl.droidsonroids.gif.GifImageView;

public class LocationDatabaseFragment extends BaseFragment{

    LocationAdapterView adapter;
    GifImageView loading;
    int position;

    DatabaseReference mDatabase;

    RequestQueue mRequestQueue;


    public LocationDatabaseFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        databaseLocationList.clear();
        mRequestQueue = Volley.newRequestQueue(getContext());

        getActivity().setTitle("Global List");

        final Bundle bundle = this.getArguments();
        position = bundle.getInt("position");

        final Bundle newBundle = new Bundle();
        newBundle.putInt("position", position);

        View view = inflater.inflate(R.layout.recyclelistfragment, container, false);
        loading = view.findViewById(R.id.loadingGif2);

        adapter = new LocationAdapterView(databaseLocationList, getActivity());


        RecyclerView mRecycler = view.findViewById(R.id.recycler);
        mRecycler.setAdapter(adapter);

        locationDatabase(loading);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);

        return view;
    }


    public void locationDatabase(final GifImageView loading) {
        databaseLocationList.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("Locations");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Location location = dataSnapshot.getValue(Location.class);
                databaseLocationList.add(new Location(location.locationId, location.locationName, location.locationLong, location.locationLat, location.locationAddress, location.locationIcon, location.locationComments));
                adapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
//              singleNews.add(new News(location.locationComments.get(0).commentName, location.locationName, location.locationComments.get(0).commentDate, location.locationIcon));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                locationDatabase(loading);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                locationDatabase(loading);
                Toast.makeText(getContext(), "Failed to load comments.", Toast.LENGTH_SHORT).show();
            }
        };

        mDatabase.addChildEventListener(childEventListener);
    }

}
