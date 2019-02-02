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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keithmaher.autismfriendlylocations.models.Location;

public class SingleLocation extends BaseActivity implements OnMapReadyCallback {

    public Context context;
    public Location aLocation;
    double lon;
    double lat;
    String name;
    MapView mapView;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_single_location, null, false);
        drawer.addView(contentView, 0);


        context = this;
        activityInfo = getIntent().getExtras();
        aLocation = getLocationObject(activityInfo.getString("locationId"));
        name = aLocation.locationName;
        lon = aLocation.locationLong;
        lat = aLocation.locationLat;
        String rating = String.valueOf(aLocation.locationRating);

        String a = String.valueOf(lon);
        String b = String.valueOf(lat);

        String c = a + "\n" + b + "\n" + a;

        setTitle(name);

        ((TextView) findViewById(R.id.singleNameET)).setText(aLocation.locationName);
        ((TextView) findViewById(R.id.singleAddressET)).setText(c);
        ((TextView) findViewById(R.id.singleRatingET)).setText(rating);

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
}
