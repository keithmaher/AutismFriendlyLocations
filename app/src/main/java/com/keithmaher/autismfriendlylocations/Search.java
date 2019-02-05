package com.keithmaher.autismfriendlylocations;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.keithmaher.autismfriendlylocations.fragments.SearchFragment;

public class Search extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search, null, false);
        drawer.addView(contentView, 0);
        setTitle("Search Locations");

        Button buttonManual = findViewById(R.id.buttonManual);
        buttonManual.setVisibility(View.GONE);

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
