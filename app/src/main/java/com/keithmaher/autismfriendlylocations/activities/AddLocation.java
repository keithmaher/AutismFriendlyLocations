package com.keithmaher.autismfriendlylocations.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        setTitle("Search All Locations");


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

    public void addManually(View v) {
        new AlertDialog.Builder(AddLocation.this)
                .setTitle("Function not available")
                .setMessage("This function will need the users current location"
                        + "\n\n"
                        + "So it will be available in version 2.0")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

}
