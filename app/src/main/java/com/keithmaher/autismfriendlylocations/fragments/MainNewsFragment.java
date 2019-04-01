package com.keithmaher.autismfriendlylocations.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.BaseActivity;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.LocationAdapterView;
import com.keithmaher.autismfriendlylocations.adapters.NewsAdapterView;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.keithmaher.autismfriendlylocations.models.News;

import pl.droidsonroids.gif.GifImageView;

public class MainNewsFragment extends BaseFragment{

    NewsAdapterView adapter;
    GifImageView loading;
    BaseActivity baseActivity;
    DatabaseReference mDatabase;
    TextView noNews;

    public MainNewsFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        newsList.clear();
        baseActivity = (BaseActivity)getActivity();
        baseActivity.navigationView.setCheckedItem(R.id.nav_home);
        getActivity().setTitle("News Feed");

        View view = inflater.inflate(R.layout.recyclelistfragment, container, false);
        loading = view.findViewById(R.id.loadingGif2);
        noNews = view.findViewById(R.id.noLocationText);
        noNews.setVisibility(View.GONE);

        adapter = new NewsAdapterView(newsList, getActivity());

        RecyclerView mRecycler = view.findViewById(R.id.recycler);
        mRecycler.setAdapter(adapter);

        locationDatabase(loading);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                loading.setVisibility(View.GONE);
                if (newsList.isEmpty()) {
                    noNews.setVisibility(View.VISIBLE);
                }
                noNews.setText("No news available");
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 5000);


        return view;
    }


    public void locationDatabase(final GifImageView loading) {
        newsList.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("Comments");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                final Iterable<DataSnapshot> contactChildren = dataSnapshot.getChildren();

                for (DataSnapshot contact : contactChildren){
                    Comment comment = contact.getValue(Comment.class);

                    newsList.add(new News(comment.getCommentName(), comment.getCommentDate(), comment.getCommentUserImageURL(), comment.getCommentLocationName()));
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);

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
