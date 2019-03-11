package com.keithmaher.autismfriendlylocations.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.ItemClickListener;
import com.keithmaher.autismfriendlylocations.adapters.LocationViewHolder;
import com.keithmaher.autismfriendlylocations.fragments.AllDBSearchFragment;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.squareup.picasso.Picasso;

public class SearchDBLocations extends BaseActivity {

    FirebaseDatabase database;
    DatabaseReference locations;
    FirebaseRecyclerAdapter<Location, LocationViewHolder> adapter;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

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

        database = FirebaseDatabase.getInstance();
        locations = database.getReference("Locations");

        recycler_menu = findViewById(R.id.recycler_category_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Location, LocationViewHolder>(Location.class,R.layout.locationrow,LocationViewHolder.class,locations) {
            @Override
            protected void populateViewHolder(LocationViewHolder viewHolder, Location model, int position) {
                viewHolder.name.setText(model.locationName);
                viewHolder.address.setText(model.locationAddress);
                Picasso.get().load(model.locationIcon).into(viewHolder.ImageView);
                final Location clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent singleLocation = new Intent(SearchDBLocations.this, SingleLocation.class);
                        singleLocation.putExtra("locationId",adapter.getRef(position).getKey());
                        singleLocation.putExtra("locationTitle", "Search");
                        startActivity(singleLocation);

                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }

}
