//package com.keithmaher.autismfriendlylocations.fragments;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.keithmaher.autismfriendlylocations.R;
//import com.keithmaher.autismfriendlylocations.adapters.UserCommentAdapterView;
//import com.keithmaher.autismfriendlylocations.models.Comment;
//import com.keithmaher.autismfriendlylocations.models.Location;
//
//import pl.droidsonroids.gif.GifImageView;
//
//
//public class UserCommentsFragment extends BaseFragment {
//
//    UserCommentAdapterView adapter;
//    GifImageView loading;
//    DatabaseReference mDatabase;
//
//
//    public UserCommentsFragment() {
//    }
//
//    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
//        userCommentList.clear();
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//
//        final Location location = getLocationObject(getContext());
//
//        getActivity().setTitle(location.getLocationName());
//
//        String locationId = location.getLocationId();
//
//        View view = inflater.inflate(R.layout.recyclelistfragment, container, false);
//        loading = view.findViewById(R.id.loadingGif2);
//
////        adapter = new UserCommentAdapterView(userCommentList, getActivity(), location);
//
//
//        RecyclerView mRecycler = view.findViewById(R.id.recycler);
//        mRecycler.setAdapter(adapter);
//
//        loadComments(loading, locationId);
//
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecycler.setLayoutManager(mLayoutManager);
//
//        return view;
//    }
//
//
//    public void loadComments(final GifImageView loading, final String locationId) {
//        userCommentList.clear();
//        mDatabase = FirebaseDatabase.getInstance().getReference("Locations").child(locationId).child("locationComments");
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                Comment comment = dataSnapshot.getValue(Comment.class);
//                if (comment.getCommentName().equals(firebaseUser.getEmail())) {
//                    userCommentList.add(new Comment(comment.getCommentName(), comment.getCommentMain(), comment.getCommentDate(), comment.getCommentUserImageURL()));
//                }
//                adapter.notifyDataSetChanged();
//                loading.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                loadComments(loading, locationId);
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                loadComments(loading, locationId);
//                Toast.makeText(getContext(), "Failed to load comments.", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        mDatabase.addChildEventListener(childEventListener);
//    }
//
//}
