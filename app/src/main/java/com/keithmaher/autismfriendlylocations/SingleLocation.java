package com.keithmaher.autismfriendlylocations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keithmaher.autismfriendlylocations.models.Location;

public class SingleLocation extends BaseActivity implements OnMapReadyCallback {

    FloatingActionButton addButton;
    public Context context;
    public Location aLocation;
    public String test;
    double lon;
    double lat;
    String name;
    String address;
    MapView mapView;

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

        if (test.contains("Search")) {
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
        myRef.push().setValue(aLocation);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Toast.makeText(SingleLocation.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(SingleLocation.this, "Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
