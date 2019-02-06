package com.keithmaher.autismfriendlylocations.activities;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Location;

public class SingleLocation extends BaseActivity implements OnMapReadyCallback {

    FloatingActionButton addButton;
    double lon;
    double lat;
    String name;
    String address;
    MapView mapView;
    Context context;
    Location aLocation;
    String test;
    FirebaseDatabase database;
    DatabaseReference myRef;

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
        aLocation = getLocationObject(activityInfo.getString("locationId"));
        test = moreinfo.getString("test");

        if (test.contains("SearchDBLocations")) {
            addButton.setVisibility(View.GONE);
        }
        name = aLocation.locationName;
        lon = aLocation.locationLong;
        lat = aLocation.locationLat;
        String likes = String.valueOf(aLocation.locationLikes);
        address = aLocation.locationAddress;

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

        for (Location c : locationList)
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

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Locations");

        final String locationId = aLocation.locationId;
        myRef.child(locationId).setValue(aLocation);

        myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String test = dataSnapshot.getKey();
                if (test.equals(locationId)){
                    Toast.makeText(context, "Already added", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SingleLocation.this, "Added", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SingleLocation.this, "FAIL", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
