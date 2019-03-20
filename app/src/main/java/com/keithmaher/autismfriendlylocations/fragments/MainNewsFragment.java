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
import com.keithmaher.autismfriendlylocations.adapters.NewsAdapterView;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.keithmaher.autismfriendlylocations.models.News;

import pl.droidsonroids.gif.GifImageView;

public class MainNewsFragment extends BaseFragment{

    NewsAdapterView adapter;
    GifImageView loading;

    int position;

    DatabaseReference mDatabase;

    RequestQueue mRequestQueue;


    public MainNewsFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        newsList.clear();

        getActivity().setTitle("News Feed");

        View view = inflater.inflate(R.layout.recyclelistfragment, container, false);
        loading = view.findViewById(R.id.loadingGif2);

        adapter = new NewsAdapterView(newsList, getActivity());


        RecyclerView mRecycler = view.findViewById(R.id.recycler);
        mRecycler.setAdapter(adapter);

        locationDatabase(loading);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);

        return view;
    }


    public void locationDatabase(final GifImageView loading) {
        newsList.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("Locations");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Location location = dataSnapshot.getValue(Location.class);
                if (location.getLocationComments() != null) {
                    for (int i = 0; i < location.getLocationComments().size(); i++) {
                        if(location.getLocationComments().get(i) == null){
                            continue;
                        }else {
                            newsList.add(new News(location.locationComments.get(i).getCommentName(), location.getLocationComments().get(i).getCommentDate(), location.getLocationComments().get(i).commentUserImageURL));

                            loading.setVisibility(View.GONE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
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
