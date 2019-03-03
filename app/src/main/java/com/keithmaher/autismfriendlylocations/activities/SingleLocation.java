package com.keithmaher.autismfriendlylocations.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.LocationListAdapter;
import com.keithmaher.autismfriendlylocations.fragments.AllDBSearchFragment;
import com.keithmaher.autismfriendlylocations.fragments.CommentFragment;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.keithmaher.autismfriendlylocations.fragments.AllDBLocationFragment.listAdapter;
import static java.time.LocalDate.now;

public class SingleLocation extends BaseActivity implements OnMapReadyCallback {

    FloatingActionButton addButton;
    double lon;
    double lat;
    String name;
    String address;
    MapView mapView;
    Context context;
    String test;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Location newLocation;
    AlertDialog.Builder alertDialog;
    AlertDialog dialog;
    EditText comment;
    EditText commentName;
    String commentname;
    String commentMain;
    Location thisLocation;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_single_location, null, false);
        drawer.addView(contentView, 0);
        addButton = findViewById(R.id.addButton);
        FrameLayout frame = findViewById(R.id.commentFrame);
        frame.setVisibility(View.GONE);

        context = this;
        activityInfo = getIntent().getExtras();
        moreinfo = getIntent().getExtras();
        test = moreinfo.getString("test");

        if (test.contains("AddLocation")) {
            thisLocation = getLocationObject(activityInfo.getString("locationId"));
            String id = thisLocation.locationId;
            for (int i = 0; i < locationDBList.size(); i ++){
                if (id.equals(locationDBList.get(i).locationId)){
                    frame.setVisibility(View.VISIBLE);
                    singleComment = locationDBList.get(i).locationComments;
                }
            }
        }

        if (test.contains("Search")) {
            frame.setVisibility(View.VISIBLE);
            thisLocation = getLocationObjectDB(activityInfo.getString("locationId"));
            addButton.setImageDrawable(getResources().getDrawable(R.drawable.comment));
            singleComment = thisLocation.locationComments;
        }

        name = thisLocation.locationName;
        lon = thisLocation.locationLong;
        lat = thisLocation.locationLat;
        address = thisLocation.locationAddress;


        setTitle(name);

        ((TextView) findViewById(R.id.singleNameET)).setText(name);
        ((TextView) findViewById(R.id.singleAddressET)).setText(address);

        // Gets the MapView from the XML layout and creates it
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (test.contains("Search")) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            new AlertDialog.Builder(mContext)
                    .setTitle(thisLocation.locationName)
                    .setMessage("Are you sure you want to delete"
                            + "\n\n"
                            + "This can not be undone!")
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            locationDBList.remove(thisLocation);
                            mDatabase = FirebaseDatabase.getInstance().getReference("Locations");
                            mDatabase.child(thisLocation.locationId).removeValue();
                            singleComment.clear();
                            startActivity(new Intent(mContext, SearchDBLocations.class));
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }



    private Location getLocationObject(String id) {

        for (Location c : allLocationList)
            if (c.locationId.equalsIgnoreCase(id))
                return c;

        return null;
    }

    private Location getLocationObjectDB(String id) {

        for (Location c : locationDBList)
            if (c.locationId.equalsIgnoreCase(id))
                return c;

        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng location = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(location)).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public void onResume() {
        mapView.onResume();

        super.onResume();
        commentFragment = CommentFragment.newInstance(); //get a new Fragment instance
        getFragmentManager().beginTransaction()
                .replace(R.id.commentFrame, commentFragment)
                .commit();// add it to the current activity
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //@Override
    public void add(View v) {
     //   super.add(v);
        if (test.contains("SearchDBLocations")) {
            alertDialog = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.commentbox, null);
            alertDialog.setView(view);
            comment = view.findViewById(R.id.editTextComment);
            commentName = view.findViewById(R.id.editTextName);
            alertDialog.setTitle(thisLocation.locationName)
                    .setMessage("Adding a comment"
                            + "\n\n"
                            + "Please let us know how you got on")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commentname = commentName.getText().toString();
                            commentMain = comment.getText().toString();
                            if (commentMain.isEmpty() || commentname.isEmpty()) {
                                Toast.makeText(context, "Please enter details", Toast.LENGTH_SHORT).show();
                            } else {
                                completeAdd();
                            }
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog.setView(view);
            dialog = alertDialog.create();
            dialog.show();
        }else {
            alertDialog = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.commentbox, null);
            alertDialog.setView(view);
            comment = view.findViewById(R.id.editTextComment);
            commentName = view.findViewById(R.id.editTextName);
            alertDialog.setTitle(thisLocation.locationName)
                    .setMessage("You are about to add this location"
                            + "\n\n"
                            + "Please leave a comment on your experience")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commentname = commentName.getText().toString();
                            commentMain = comment.getText().toString();
                            if (commentMain.isEmpty() || commentname.isEmpty()) {
                                Toast.makeText(context, "Please enter details", Toast.LENGTH_SHORT).show();
                            } else {
                                completeAdd();
                            }
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog.setView(view);
            dialog = alertDialog.create();
            dialog.show();
        }

    }

    @SuppressLint("SimpleDateFormat")
    public void completeAdd(){
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("dd-MM-yyyy").format(cDate);
        if (test.contains("SearchDBLocations")) {
            singleComment.add(new Comment(commentname, commentMain, fDate));
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Locations");
            String locationId = thisLocation.locationId;
            newLocation = new Location(thisLocation.locationId, thisLocation.locationName, thisLocation.locationLong, thisLocation.locationLat, thisLocation.locationAddress, thisLocation.locationIcon, singleComment);
            databaseCheckDB();
            myRef.child(locationId).child("locationComments").setValue(singleComment);
        }else if (test.contains("Add")){
            singleComment.add(new Comment(commentname, commentMain, fDate));
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Locations");
            newLocation = new Location(thisLocation.locationId, thisLocation.locationName, thisLocation.locationLong, thisLocation.locationLat, thisLocation.locationAddress, thisLocation.locationIcon, singleComment);
            String id = thisLocation.locationId;
            for (int i = 0; i < locationDBList.size(); i ++){
                if (id.equals(locationDBList.get(i).locationId)){
                    List<Comment> emptyList = new ArrayList<>();
                    emptyList.add(new Comment(commentname, commentMain, fDate));
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("Locations");
                    newLocation = new Location(thisLocation.locationId, thisLocation.locationName, thisLocation.locationLong, thisLocation.locationLat, thisLocation.locationAddress, thisLocation.locationIcon, emptyList);
                }
            }
            databaseCheck();
            myRef.child(thisLocation.locationId).setValue(newLocation);
        }

    }

    public void databaseCheck(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild(thisLocation.locationId)) {
                    new AlertDialog.Builder(SingleLocation.this)
                            .setTitle(thisLocation.locationName)
                            .setMessage("This Location already in our system"
                                    + "\n\n"
                                    + "But we added your comment to " +thisLocation.locationName)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(SingleLocation.this, SearchDBLocations.class));
                                }
                            })
                            .show();

                }else{
                    new AlertDialog.Builder(SingleLocation.this)
                            .setTitle(thisLocation.locationName)
                            .setMessage("Addition Complete"
                                    + "\n\n"
                                    + "Thank you for your addition to our system")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(SingleLocation.this, SearchDBLocations.class));
                                }
                            })
                            .show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void databaseCheckDB(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild(thisLocation.locationId)) {
                    new AlertDialog.Builder(SingleLocation.this)
                            .setTitle(thisLocation.locationName)
                            .setMessage("Comment added successfully"
                                    + "\n\n"
                                    + "Thank you")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();

                }else{
                    new AlertDialog.Builder(SingleLocation.this)
                            .setTitle(thisLocation.locationName)
                            .setMessage("Addition Complete"
                                    + "\n\n"
                                    + "Thank you")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
