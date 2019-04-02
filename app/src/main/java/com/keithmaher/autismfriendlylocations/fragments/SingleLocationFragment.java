package com.keithmaher.autismfriendlylocations.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.BaseActivity;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.ViewPagerAdapter;
import com.keithmaher.autismfriendlylocations.models.Location;

public class SingleLocationFragment extends BaseFragment implements OnMapReadyCallback {
    Location location;
    DatabaseReference mDatabase;
    LatLng mapLocation;
    LatLng myLocation;
    MapView mapView;
    BaseActivity baseActivity;

    public SingleLocationFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        View view = inflater.inflate(R.layout.singlelocationfragment, container, false);
        location = getLocationObject(getContext());

        baseActivity = (BaseActivity)getActivity();

        TabLayout tabLayout;
        ViewPager viewPager;
        viewPager = view.findViewById(R.id.single_viewpager);
        setupViewPager(viewPager);
        tabLayout = view.findViewById(R.id.single_tabs);
        tabLayout.setupWithViewPager(viewPager);
        getActivity().setTitle(location.locationName);

        double lon = location.getLocationLong();
        double lat = location.getLocationLat();
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapLocation = new LatLng(lat, lon);

        double myLat = Double.parseDouble(baseActivity.GPSLocationLAT);
        double myLng = Double.parseDouble(baseActivity.GPSLocationLNG);
        myLocation = new LatLng(myLat, myLng);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(locationInfoFragment(), "Information");
        adapter.addFragment(commentFragment(), "Comments");

        viewPager.setAdapter(adapter);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.addMarker(new MarkerOptions().position(mapLocation)).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLocation, 15));

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
