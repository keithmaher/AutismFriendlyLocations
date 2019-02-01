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
        setTitle("AllLocations");


        //input details to list for now
        Location c = new Location(12545445, "This is a test", 22.0121, 55.254, 2.12345f, "Nice place", false);
        Location d = new Location(12545445, "This is a test2", 22.0121, 55.254, 2.12345f, "Nice place", false);
        locationList.add(c);
        locationList.add(d);

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
