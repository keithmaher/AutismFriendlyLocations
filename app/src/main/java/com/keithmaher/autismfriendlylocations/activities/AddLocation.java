package com.keithmaher.autismfriendlylocations.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.fragments.AllSearchFragment;

public class AddLocation extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search, null, false);
        drawer.addView(contentView, 0);
        setTitle("Add1 Location");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Location","Home : " + allLocationList);

        allLocationFragment = AllSearchFragment.newInstance(); //get a new Fragment instance
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, allLocationFragment)
                .commit();// add it to the current activity
    }
}
