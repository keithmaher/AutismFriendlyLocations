package com.keithmaher.autismfriendlylocations.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.Utils.TinyDB;
import com.keithmaher.autismfriendlylocations.adapters.CommentAdapterView;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class CommentFragment extends BaseFragment {

    DatabaseReference mDatabase;
    DatabaseReference newDatabase;
    List<Comment> singleComment = new ArrayList<>();
    CommentAdapterView adapter;
    GifImageView loading;
    FloatingActionButton addCommentButton;
    String commentMain;
    EditText comment;
    TextView commentName;
    TextView noCommentText;

    public CommentFragment() {
    }

    @SuppressLint("RestrictedApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locationCommentList.clear();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String userEmail = firebaseUser.getEmail();

        final Location location = getLocationObject(getContext());

        String locationId = location.getLocationId();

        newDatabase = FirebaseDatabase.getInstance().getReference("Comments").child(location.getLocationId()).push();

        View view = inflater.inflate(R.layout.singlelocationcommentfragment, container, false);
        noCommentText = view.findViewById(R.id.noCommentText);
        noCommentText.setVisibility(View.GONE);
        loading = view.findViewById(R.id.loadingGif);
        addCommentButton = view.findViewById(R.id.addCommentButton);
        addCommentButton.setVisibility(View.GONE);
        adapter = new CommentAdapterView(locationCommentList, getActivity());


        RecyclerView mRecycler = view.findViewById(R.id.recycler_category_menu1);
        mRecycler.setAdapter(adapter);

        locationComments(loading, locationId);

        addComments(location, addCommentButton, userEmail);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);


        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                loading.setVisibility(View.GONE);
                if (locationCommentList.isEmpty()) {
                    noCommentText.setVisibility(View.VISIBLE);
                }
                noCommentText.setText(location.getLocationName()+" is not in our system\nBe the first to comment by adding it to our list");
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 5000);




        return view;
    }

    private void addComments(final Location location, FloatingActionButton addCommentButton, final String userEmail) {

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                v = getLayoutInflater().inflate(R.layout.commentalertbox, null);
                alertDialog.setView(v);
                comment = v.findViewById(R.id.editTextComment);
                commentName = v.findViewById(R.id.editTextName);
                commentName.setText(userEmail);
                alertDialog.setTitle(location.locationName)
                        .setMessage("Adding a comment"
                                + "\n\n"
                                + "Please let us know how you got on")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                commentMain = comment.getText().toString();
                                if (commentMain.isEmpty()) {
                                    Toast.makeText(getContext(), "Please enter a comment", Toast.LENGTH_SHORT).show();
                                } else {
                                    Date cDate = new Date();
                                    String fDate = new SimpleDateFormat("dd-MM-yyyy").format(cDate);

                                    if(firebaseUser.getPhotoUrl() != null) {
                                        newDatabase.setValue(new Comment(userEmail, commentMain, fDate, firebaseUser.getPhotoUrl().toString(), location.getLocationName()));
                                    }else{
                                        newDatabase.setValue(new Comment(userEmail, commentMain, fDate, location.getLocationName()));
                                    }

//                                    if (location.getLocationComments() == null){
//                                        singleComment = locationCommentList;
//                                        if(firebaseUser.getPhotoUrl() != null) {
//                                            singleComment.add(new Comment(userEmail, commentMain, fDate, firebaseUser.getPhotoUrl().toString()));
//                                        }else{
//                                            singleComment.add(new Comment(userEmail, commentMain, fDate));
//                                        }
//                                        mDatabase.setValue(singleComment);
//                                        TinyDB tinydb = new TinyDB(getContext());
//                                        tinydb.putObject("concertObj", location);
//                                        BaseFragment.singleLocationFragment(getActivity());
//                                    }else{
//                                        if (firebaseUser.getPhotoUrl() != null){
//                                            singleComment.add(new Comment(userEmail, commentMain, fDate, firebaseUser.getPhotoUrl().toString()));
//                                        }else{
//                                            singleComment.add(new Comment(userEmail, commentMain, fDate));
//                                        }

//                                      Location newLocation = new Location(location.getLocationId(), location.getLocationName(), location.getLocationLong(), location.getLocationLat(), location.getLocationAddress(), location.locationIcon, singleComment);
//                                        mDatabase.setValue(singleComment);
//                                      BaseFragment.databaseLocationFragment(getActivity());
//                                    }
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.setView(v);
                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });

    }

    private void locationComments(final GifImageView loading, final String locationId) {

        locationCommentList.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference("Comments").child(locationId);
        ChildEventListener childEventListener = new ChildEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                if (comment.commentUserImageURL == null){
                    locationCommentList.add(new Comment(comment.commentName, comment.commentMain, comment.commentDate, comment.commentLocationName));
                }else {
                    locationCommentList.add(new Comment(comment.commentName, comment.commentMain, comment.commentDate, comment.commentUserImageURL, comment.commentLocationName));
                }
                adapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
                addCommentButton.setVisibility(View.VISIBLE);
//              singleNews.add(new News(location.locationComments.get(0).commentName, location.locationName, location.locationComments.get(0).commentDate, location.locationIcon));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                locationComments(loading, locationId);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                locationComments(loading, locationId);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load comments.", Toast.LENGTH_SHORT).show();
            }
        };

        mDatabase.addChildEventListener(childEventListener);
    }
}
