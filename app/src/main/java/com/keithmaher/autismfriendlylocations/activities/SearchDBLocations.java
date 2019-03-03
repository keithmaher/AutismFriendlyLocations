package com.keithmaher.autismfriendlylocations.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.fragments.AllDBSearchFragment;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;

public class SearchDBLocations extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search, null, false);
        drawer.addView(contentView, 0);
        setTitle("My List");

        mContext = this;
        Button buttonManual = findViewById(R.id.buttonManual);
        buttonManual.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        allDBLocationFragment = AllDBSearchFragment.newInstance(); //get a new Fragment instance
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, allDBLocationFragment)
                .commit();// add it to the current activity
    }

}
