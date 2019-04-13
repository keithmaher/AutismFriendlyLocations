package com.keithmaher.autismfriendlylocations.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.UserCommentAdapterView;
import com.keithmaher.autismfriendlylocations.models.Comment;

import pl.droidsonroids.gif.GifImageView;

public class LocationUserFragment extends BaseFragment{

    UserCommentAdapterView adapter;
    GifImageView loading;

    DatabaseReference mDatabase;
    TextView noComments;
    RequestQueue mRequestQueue;


    public LocationUserFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        databaseLocationList.clear();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mRequestQueue = Volley.newRequestQueue(getContext());

        getActivity().setTitle("My Comments");

        View view = inflater.inflate(R.layout.recyclelistfragment, container, false);
        loading = view.findViewById(R.id.loadingGif2);
        noComments = view.findViewById(R.id.noLocationText);
        noComments.setVisibility(View.GONE);
        locationDatabase(loading);

        adapter = new UserCommentAdapterView(userCommentList, getActivity());

        RecyclerView mRecycler = view.findViewById(R.id.recycler);
        mRecycler.setAdapter(adapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);


        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                loading.setVisibility(View.GONE);
                if (userCommentList.isEmpty()) {
                    noComments.setVisibility(View.VISIBLE);
                }
                noComments.setText("You have no comments");
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 5000);

        return view;
    }


    public void locationDatabase(final GifImageView loading) {
        userCommentList.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("Comments");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                final Iterable<DataSnapshot> contactChildren = dataSnapshot.getChildren();

                for (DataSnapshot contact : contactChildren){
                    Comment comment = contact.getValue(Comment.class);

                    if (comment.getCommentName().equals(firebaseUser.getEmail())){
                        userCommentList.add(new Comment(comment.getCommentName(),comment.getCommentMain(), comment.getCommentDate(), comment.getCommentLocationName()));
                        adapter.notifyDataSetChanged();
                        loading.setVisibility(View.GONE);
                    }
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
