package com.keithmaher.autismfriendlylocations.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.ItemClickListener;
import com.keithmaher.autismfriendlylocations.adapters.LocationViewHolder;
import com.keithmaher.autismfriendlylocations.fragments.AllSearchFragment;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddLocation extends BaseActivity {

    FirebaseRecyclerAdapter<Location, LocationViewHolder> adapter;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search, null, false);
        drawer.addView(contentView, 0);
        setTitle("Search All Locations");

        mContext = this;

        database = FirebaseDatabase.getInstance();
        locations = database.getReference("APILocations");
        database.getReference("APILocations").removeValue();

        recycler_menu = findViewById(R.id.recycler_category_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        apiCall();

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

                        Intent singleLocation = new Intent(AddLocation.this, SingleLocation.class);
                        singleLocation.putExtra("locationId",adapter.getRef(position).getKey());
                        singleLocation.putExtra("locationTitle", "Add");
                        startActivity(singleLocation);

                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
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
