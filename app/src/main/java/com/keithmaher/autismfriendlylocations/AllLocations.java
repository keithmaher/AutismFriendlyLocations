package com.keithmaher.autismfriendlylocations;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.keithmaher.autismfriendlylocations.fragments.LocationFragment;
import com.keithmaher.autismfriendlylocations.models.Location;

public class AllLocations extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_alllocations, null, false);
        drawer.addView(contentView, 0);
        setTitle("All Locations");

        //input details to list for now
        Location a = new Location("1231", "Costa", -7.7207914, 52.3508738, 2.12345f, "Nice place", false);
        Location b = new Location("2542", "This is a test2", 22.0121, 55.254, 2.12345f, "Nice place", false);
        Location c = new Location("1233", "This is a test", 22.0121, 55.254, 2.12345f, "Nice place", false);
        Location d = new Location("2544", "Hello", 22.0121, 55.254, 2.12345f, "Nice place", false);
        Location e = new Location("1235", "This is a test", 22.0121, 55.254, 2.12345f, "Nice place", false);
        Location f = new Location("2546", "zara", 22.0121, 55.254, 2.12345f, "Nice place", false);
        Location g = new Location("1237", "This is a test", 22.0121, 55.254, 2.12345f, "Nice place", false);
        Location h = new Location("2548", "WIT", 22.0121, 55.254, 2.12345f, "Nice place", false);
        locationList.add(c);
        locationList.add(b);
        locationList.add(a);
        locationList.add(d);
        locationList.add(e);
        locationList.add(f);
        locationList.add(g);
        locationList.add(h);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Location","Home : " + locationList);

        locationFragment = LocationFragment.newInstance(); //get a new Fragment instance
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, locationFragment)
                .commit();// add it to the current activity
    }

}
