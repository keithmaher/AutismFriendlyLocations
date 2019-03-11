package com.keithmaher.autismfriendlylocations.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.CommentViewHolder;
import com.keithmaher.autismfriendlylocations.adapters.ItemClickListener;
import com.keithmaher.autismfriendlylocations.adapters.LocationViewHolder;
import com.keithmaher.autismfriendlylocations.fragments.CommentFragment;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;

public class SingleLocation extends BaseActivity implements OnMapReadyCallback {

    FirebaseRecyclerAdapter<Comment, CommentViewHolder> adapter;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    Location location1;
    LatLng mapLocation;
//    List<Location> newList;
    FloatingActionButton addButton;
    String locationTitle;
    String locationID;
    double lon;
    double lat;
    MapView mapView;
    Context context;
    String locationName;
    String locationAddress;
    String test123;
    String testin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_single_location, null, false);
        drawer.addView(contentView, 0);
        addButton = findViewById(R.id.addButton);
//        FrameLayout frame = findViewById(R.id.commentFrame);
//        frame.setVisibility(View.GONE);

        context = this;
        activityInfo = getIntent().getExtras();
        locationTitle = activityInfo.getString("locationTitle");
        locationID = activityInfo.getString("locationId");
        if(locationTitle.equals("Search")){
            testin = "Locations";
            addButton.setImageDrawable(getResources().getDrawable(R.drawable.comment));
        }else{
            testin = "APILocations";
        }

        recycler_menu = findViewById(R.id.recycler_category_menu1);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        getLocationObject(locationID, testin);


//        if (test.contains("AddLocation")) {
//            thisLocation = getLocationObject(activityInfo.getString("locationId"));
//            String id = thisLocation.locationId;
//            for (int i = 0; i < locationDBList.size(); i ++){
//                if (id.equals(locationDBList.get(i).locationId)){
//                    frame.setVisibility(View.VISIBLE);
//                    singleComment = locationDBList.get(i).locationComments;
//                }
//            }
//        }
//
//        if (test.contains("Search")) {
//            frame.setVisibility(View.VISIBLE);
//            thisLocation = getLocationObjectDB(activityInfo.getString("locationId"));
//            addButton.setImageDrawable(getResources().getDrawable(R.drawable.comment));
//            singleComment = thisLocation.locationComments;
//        }
//
//        name = thisLocation.locationName;
//        lon = thisLocation.locationLong;
//        lat = thisLocation.locationLat;
//        address = thisLocation.locationAddress;
//
//
//        setTitle(name);
//
//        Toast.makeText(context, locationName, Toast.LENGTH_SHORT).show();
//        ((TextView) findViewById(R.id.singleNameET)).setText(testing);
//        ((TextView) findViewById(R.id.singleAddressET)).setText(locationAddress);
//
        // Gets the MapView from the XML layout and creates it
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (locationTitle.contains("Search")) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }else{
            return false;
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//
//            new AlertDialog.Builder(mContext)
//                    .setTitle(thisLocation.locationName)
//                    .setMessage("Are you sure you want to delete"
//                            + "\n\n"
//                            + "This can not be undone!")
//                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            locationDBList.remove(thisLocation);
//                            mDatabase = FirebaseDatabase.getInstance().getReference("Locations");
//                            mDatabase.child(thisLocation.locationId).removeValue();
//                            singleComment.clear();
//                            startActivity(new Intent(mContext, SearchDBLocations.class));
//                        }
//                    })
//                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    })
//                    .show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }



    public void getLocationObject(final String id, final String test) {

        FirebaseDatabase.getInstance().getReference(test)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals(id)) {
                                location1 = snapshot.getValue(Location.class);
                                locationName = location1.getLocationName();
                                locationAddress = location1.getLocationAddress();
                                lon = location1.getLocationLong();
                                lat = location1.getLocationLat();

                                mapLocation = new LatLng(lat, lon);

                                ((TextView) findViewById(R.id.singleNameET)).setText(locationName);
                                ((TextView) findViewById(R.id.singleAddressET)).setText(locationAddress);

                                database = FirebaseDatabase.getInstance();
                                locations = database.getReference(test).child(id).child("locationComments");

                                adapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(Comment.class,R.layout.commentrow,CommentViewHolder.class,locations) {
                                    @Override
                                    protected void populateViewHolder(CommentViewHolder viewHolder, final Comment model, int position) {
                                        viewHolder.name.setText(model.commentName);
                                        viewHolder.address.setText(model.commentDate);
//                                        Picasso.get().load(model.c).into(viewHolder.ImageView);
                                        final Location clickItem = model;
                                        viewHolder.setItemClickListener(new ItemClickListener() {
                                            @Override
                                            public void onClick(View view, int position, boolean isLongClick) {

                                                new AlertDialog.Builder(mContext)
                                                .setTitle(model.commentName)
                                                .setMessage(model.commentMain
                                                        + "\n\n"
                                                        + "Date added: "+model.commentDate)
                                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                })
                                                .show();

                                            }
                                        });
                                    }
                                };
                                recycler_menu.setAdapter(adapter);

                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

//    public void testing(Location location1){
//        location = location1;
//        newList.add(location);
//    }
//
//    private Location getLocationObjectDB(String id) {
//
//        for (Location c : locationDBList)
//            if (c.locationId.equalsIgnoreCase(id))
//                return c;
//
//        return null;
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.addMarker(new MarkerOptions().position(mapLocation)).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLocation, 15));
    }

    @Override
    public void onResume() {
        mapView.onResume();

        super.onResume();
//        commentFragment = CommentFragment.newInstance(); //get a new Fragment instance
//        getFragmentManager().beginTransaction()
//                .replace(R.id.commentFrame, commentFragment)
//                .commit();// add it to the current activity
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

//    //@Override
//    public void add(View v) {
//     //   super.add(v);
//        if (test.contains("SearchDBLocations")) {
//            alertDialog = new AlertDialog.Builder(this);
//            View view = getLayoutInflater().inflate(R.layout.commentbox, null);
//            alertDialog.setView(view);
//            comment = view.findViewById(R.id.editTextComment);
//            commentName = view.findViewById(R.id.editTextName);
//            alertDialog.setTitle(thisLocation.locationName)
//                    .setMessage("Adding a comment"
//                            + "\n\n"
//                            + "Please let us know how you got on")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            commentname = commentName.getText().toString();
//                            commentMain = comment.getText().toString();
//                            if (commentMain.isEmpty() || commentname.isEmpty()) {
//                                Toast.makeText(context, "Please enter details", Toast.LENGTH_SHORT).show();
//                            } else {
//                                completeAdd();
//                            }
//                        }
//                    })
//                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//            alertDialog.setView(view);
//            dialog = alertDialog.create();
//            dialog.show();
//        }else {
//            alertDialog = new AlertDialog.Builder(this);
//            View view = getLayoutInflater().inflate(R.layout.commentbox, null);
//            alertDialog.setView(view);
//            comment = view.findViewById(R.id.editTextComment);
//            commentName = view.findViewById(R.id.editTextName);
//            alertDialog.setTitle(thisLocation.locationName)
//                    .setMessage("You are about to add this location"
//                            + "\n\n"
//                            + "Please leave a comment on your experience")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            commentname = commentName.getText().toString();
//                            commentMain = comment.getText().toString();
//                            if (commentMain.isEmpty() || commentname.isEmpty()) {
//                                Toast.makeText(context, "Please enter details", Toast.LENGTH_SHORT).show();
//                            } else {
//                                completeAdd();
//                            }
//                        }
//                    })
//                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//            alertDialog.setView(view);
//            dialog = alertDialog.create();
//            dialog.show();
//        }
//
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    public void completeAdd(){
//        Date cDate = new Date();
//        String fDate = new SimpleDateFormat("dd-MM-yyyy").format(cDate);
//        if (test.contains("SearchDBLocations")) {
//            singleComment.add(new Comment(commentname, commentMain, fDate));
//            database = FirebaseDatabase.getInstance();
//            myRef = database.getReference("Locations");
//            String locationId = thisLocation.locationId;
//            newLocation = new Location(thisLocation.locationId, thisLocation.locationName, thisLocation.locationLong, thisLocation.locationLat, thisLocation.locationAddress, thisLocation.locationIcon, singleComment);
//            databaseCheckDB();
//            myRef.child(locationId).child("locationComments").setValue(singleComment);
//        }else if (test.contains("Add")){
//            singleComment.add(new Comment(commentname, commentMain, fDate));
//            database = FirebaseDatabase.getInstance();
//            myRef = database.getReference("Locations");
//            newLocation = new Location(thisLocation.locationId, thisLocation.locationName, thisLocation.locationLong, thisLocation.locationLat, thisLocation.locationAddress, thisLocation.locationIcon, singleComment);
//            String id = thisLocation.locationId;
//            for (int i = 0; i < locationDBList.size(); i ++){
//                if (id.equals(locationDBList.get(i).locationId)){
//                    List<Comment> emptyList = new ArrayList<>();
//                    emptyList.add(new Comment(commentname, commentMain, fDate));
//                    database = FirebaseDatabase.getInstance();
//                    myRef = database.getReference("Locations");
//                    newLocation = new Location(thisLocation.locationId, thisLocation.locationName, thisLocation.locationLong, thisLocation.locationLat, thisLocation.locationAddress, thisLocation.locationIcon, emptyList);
//                }
//            }
//            databaseCheck();
//            myRef.child(thisLocation.locationId).setValue(newLocation);
//        }
//
//    }
//
//    public void databaseCheck(){
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                if (snapshot.hasChild(thisLocation.locationId)) {
//                    new AlertDialog.Builder(SingleLocation.this)
//                            .setTitle(thisLocation.locationName)
//                            .setMessage("This Location already in our system"
//                                    + "\n\n"
//                                    + "But we added your comment to " +thisLocation.locationName)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivity(new Intent(SingleLocation.this, SearchDBLocations.class));
//                                }
//                            })
//                            .show();
//
//                }else{
//                    new AlertDialog.Builder(SingleLocation.this)
//                            .setTitle(thisLocation.locationName)
//                            .setMessage("Addition Complete"
//                                    + "\n\n"
//                                    + "Thank you for your addition to our system")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivity(new Intent(SingleLocation.this, SearchDBLocations.class));
//                                }
//                            })
//                            .show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//    public void databaseCheckDB(){
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                if (snapshot.hasChild(thisLocation.locationId)) {
//                    new AlertDialog.Builder(SingleLocation.this)
//                            .setTitle(thisLocation.locationName)
//                            .setMessage("Comment added successfully"
//                                    + "\n\n"
//                                    + "Thank you")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            })
//                            .show();
//
//                }else{
//                    new AlertDialog.Builder(SingleLocation.this)
//                            .setTitle(thisLocation.locationName)
//                            .setMessage("Addition Complete"
//                                    + "\n\n"
//                                    + "Thank you")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            })
//                            .show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }


}
