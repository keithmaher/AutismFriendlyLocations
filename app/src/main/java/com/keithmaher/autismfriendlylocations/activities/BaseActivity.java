package com.keithmaher.autismfriendlylocations.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.fragments.AllLocationFragment;
import com.keithmaher.autismfriendlylocations.fragments.AllDBLocationFragment;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Bundle activityInfo; // Used for persistence (of sorts)
    public Bundle moreinfo; // Used for persistence (of sorts)
    public AllLocationFragment allLocationFragment;
    public AllDBLocationFragment allDBLocationFragment;
    public static List<Location> allLocationList = new ArrayList<>();
    public static List<Location> locationDBList = new ArrayList<>();
    public static List<Comment> locationComments = new ArrayList<>();
    protected DrawerLayout drawer;
    public static String locationName;
    public static String locationId;
    public static String locationRoad;
    public static String locationCity;
    public static String locationState;
    public static double locationLng;
    public static double locationLat;
    RequestQueue mRequestQueue;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Location thisLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_base);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            mRequestQueue = Volley.newRequestQueue(this);
            if (allLocationList.isEmpty()) apiCall();
            if (locationDBList.isEmpty()) databasePull();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //startActivity(new Intent(this, Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            startActivity(new Intent(this, SearchDBLocations.class));
        } else if (id == R.id.nav_add) {
            startActivity(new Intent(this, AddLocation.class));
        }
//        else if (id == R.id.nav_login) {
//
//        } else if (id == R.id.nav_logout) {
//
//        }
        else if (id == R.id.nav_home) {
            startActivity(new Intent(this, Home.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void add(View v){
        //Toast.makeText(this, "Testing", Toast.LENGTH_SHORT).show();
    }

    public void apiCall() {

        String url = "https://api.foursquare.com/v2/venues/search?near=Clonmel,IE&v=28012019&limit=100&client_id=LIKFRNK34TNZQHOVJXSZEQNEFRGFS12VGLXRSHZBZKCG54XV&client_secret=EYNO0LDUNISNP2XBQIWZYP231NENGUA2GTYFFFHQAKGZGEFV";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    //loop trough the data array to pull out the needed information
                    JSONObject locationInformation = response.getJSONObject("response");
                    JSONArray venueInformation = locationInformation.getJSONArray("venues");
                    String iconUrl = "";
                    String mainAddress;

                    for (int i = 0; i < venueInformation.length(); i++) {
                        JSONObject locationObject = venueInformation.getJSONObject(i);
                        locationName = locationObject.optString("name");
                        locationId = locationObject.optString("id");
                        JSONObject address = locationObject.getJSONObject("location");
                        JSONArray categotie  = locationObject.getJSONArray("categories");
                        for (int a = 0; a < categotie.length(); a++){
                            JSONObject catObject = categotie.getJSONObject(0);
                            String cat = catObject.optString("name");
                            JSONObject icon = catObject.getJSONObject("icon");
                            String main = icon.optString("prefix");
                            String end = icon.optString("suffix");
                            iconUrl = main+"bg_100"+end;
                        }
                        locationRoad = address.optString("address");
                        locationCity = address.optString("city");
                        locationState = address.optString("state");
                        locationLat = Double.parseDouble(address.optString("lat"));
                        locationLng = Double.parseDouble(address.optString("lng"));
                        if (locationRoad.isEmpty()){
                            mainAddress = locationCity + "\n" + locationState;
                        }else {
                            mainAddress = locationRoad + "\n" + locationCity + "\n" + locationState;
                        }
                        if (locationName.isEmpty()||locationLat==0||locationLng==0||mainAddress.length()<3||locationId.isEmpty()){

                        }else{
                            allLocationList.add(new Location(locationId, locationName, locationLng, locationLat, mainAddress, iconUrl));
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        // Access the RequestQueue through your singleton class.
        mRequestQueue.add(jsonRequest);
    }

    public void databasePull() {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Locations");

        myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("locationName").getValue().toString();
                String address = dataSnapshot.child("locationAddress").getValue().toString();
                double lng = Double.parseDouble(dataSnapshot.child("locationLong").getValue().toString());
                double lat = Double.parseDouble(dataSnapshot.child("locationLat").getValue().toString());
                String id = dataSnapshot.child("locationId").getValue().toString();
                String icon = dataSnapshot.child("locationIcon").getValue().toString();

                locationDBList.add(new Location(id, name, lng, lat, address, icon));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }

}
