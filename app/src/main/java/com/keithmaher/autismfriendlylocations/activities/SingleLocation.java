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
import android.view.View;
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
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;

import java.util.ArrayList;
import java.util.Date;

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

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_single_location, null, false);
        drawer.addView(contentView, 0);
        addButton = findViewById(R.id.addButton);

        context = this;
        activityInfo = getIntent().getExtras();
        moreinfo = getIntent().getExtras();
        thisLocation = getLocationObject(activityInfo.getString("locationId"));
        test = moreinfo.getString("test");

        if (test.contains("SearchDBLocations")) {
            addButton.setVisibility(View.GONE);
        }
        name = thisLocation.locationName;
        lon = thisLocation.locationLong;
        lat = thisLocation.locationLat;
        String likes = String.valueOf(thisLocation.locationLikes);
        address = thisLocation.locationAddress;

        setTitle(name);

        ((TextView) findViewById(R.id.singleNameET)).setText(name);
        ((TextView) findViewById(R.id.singleAddressET)).setText(address);
        ((TextView) findViewById(R.id.singleRatingET)).setText(likes);

        // Gets the MapView from the XML layout and creates it
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }



    private Location getLocationObject(String id) {

        for (Location c : allLocationList)
            if (c.locationId.equalsIgnoreCase(id))
                return c;

        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng location = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(location).title(name)).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
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

    @Override
    public void add(View v) {
        super.add(v);

        new AlertDialog.Builder(this)
            .setTitle(thisLocation.locationName)
            .setMessage("You are about to add this location"
                    + "\n\n"
                    + "have you been here lately, if so please leave a comment on your experience")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    completeAdd();
                    //databaseCheck();
                }
            })
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            })
            .show();

    }

    public void completeAdd(){
        locationComments.add(new Comment("123","Keith Maher", "Testing Comments"));
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Locations");
        final Date date = new Date();
        final String locationId = thisLocation.locationId;
        newLocation = new Location(thisLocation.locationId, thisLocation.locationName,
                thisLocation.locationLong, thisLocation.locationLat,
                thisLocation.locationAddress, 0, thisLocation.locationIcon,
                "Keith Maher", date.toString(), locationComments);

        databaseCheck();
        myRef.child(locationId).setValue(newLocation);
        //Toast.makeText(context, a, Toast.LENGTH_SHORT).show();

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
                                    + "Someone has already added this location to our system, Maybe leave a comment on your visit")
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


}
