package com.keithmaher.autismfriendlylocations;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.keithmaher.autismfriendlylocations.fragments.LocationFragment;
import com.keithmaher.autismfriendlylocations.fragments.SearchFragment;
import com.keithmaher.autismfriendlylocations.models.Location;

public class Search extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search, null, false);
        drawer.addView(contentView, 0);
        setTitle("Search Locations");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Location","Home : " + locationList);

        locationFragment = SearchFragment.newInstance(); //get a new Fragment instance
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, locationFragment)
                .commit();// add it to the current activity
    }
}
