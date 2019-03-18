package com.keithmaher.autismfriendlylocations.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocationInformationFragment extends BaseFragment {

    DatabaseReference mDatabase;
    ArrayList<Comment> singleComment = new ArrayList<>();
    String commentMain;
    EditText comment;
    TextView commentName;

    public LocationInformationFragment() {
    }

    @SuppressLint("RestrictedApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference("Locations");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String userEmail = firebaseUser.getEmail();

        View view = inflater.inflate(R.layout.singlelocationinformationfragment, container, false);

        final Location location = getLocationObject(getContext());

        TextView name = view.findViewById(R.id.singleNameET);
        TextView address = view.findViewById(R.id.singleAddressET);
        name.setText(location.getLocationName());
        address.setText(location.getLocationAddress());
        final FloatingActionButton addLocationButton = view.findViewById(R.id.addLocationButton);
        addLocationButton.setVisibility(View.GONE);

        databaseCheckDB(location, addLocationButton, userEmail);


        return view;
    }

    public void databaseCheckDB(final Location location, final FloatingActionButton addLocationButton, final String userEmail){

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild(location.getLocationId())) {

                    addLocationButton.setVisibility(View.GONE);

                }else{
                    addLocationButton.setVisibility(View.VISIBLE);
                    addLocationButton.setOnClickListener(new View.OnClickListener() {
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
                                                Toast.makeText(getContext(), "Please enter details", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Date cDate = new Date();
                                                String fDate = new SimpleDateFormat("dd-MM-yyyy").format(cDate);
                                                if(firebaseUser.getPhotoUrl() != null) {
                                                    singleComment.add(new Comment(userEmail, commentMain, fDate, firebaseUser.getPhotoUrl().toString()));
                                                }else{
                                                    singleComment.add(new Comment(userEmail, commentMain, fDate));
                                                }
                                                Location newLocation = new Location(location.getLocationId(), location.getLocationName(), location.getLocationLong(), location.getLocationLat(), location.getLocationAddress(), location.locationIcon, singleComment);
                                                mDatabase.child(location.getLocationId()).setValue(newLocation);
                                                BaseFragment.databaseLocationFragment(getActivity());
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

