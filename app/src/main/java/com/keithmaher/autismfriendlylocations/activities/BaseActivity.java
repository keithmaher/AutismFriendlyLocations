package com.keithmaher.autismfriendlylocations.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.Toast;

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
import com.keithmaher.autismfriendlylocations.fragments.CommentFragment;
import com.keithmaher.autismfriendlylocations.fragments.NewsFragment;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.keithmaher.autismfriendlylocations.models.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.*;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Bundle activityInfo; // Used for persistence (of sorts)
    public Bundle moreinfo; // Used for persistence (of sorts)
    public AllLocationFragment allLocationFragment;
    public AllDBLocationFragment allDBLocationFragment;
    public NewsFragment newsFragment;
    public CommentFragment commentFragment;
    public static List<Location> allLocationList = new ArrayList<>();
    public static List<Location> locationDBList = new ArrayList<>();
//    public static List<Comment> locationComments = new ArrayList<>();
    public static List<Comment> singleComment = new ArrayList<>();
    public static List<News> singleNews = new ArrayList<>();
    protected DrawerLayout drawer;
    public static String locationName;
    public static String locationId;
    public static String locationRoad;
    public static String locationCity;
    public static String locationState;
    public static double locationLng;
    public static double locationLat;
    RequestQueue mRequestQueue;
    DatabaseReference mDatabase;
    FirebaseDatabase database;
    DatabaseReference locations;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRequestQueue = Volley.newRequestQueue(this);

//        if(mContext.toString().contains("Home") || mContext.toString().contains("Add")) {
//
//            final ProgressDialog progress = new ProgressDialog(this);
//            progress.setTitle("Collecting Data");
//            progress.setMessage("Please be patient...");
//            progress.show();
//
//            Runnable progressRunnable = new Runnable() {
//
//                @Override
//                public void run() {
//                    progress.cancel();
//                }
//            };
//
//            Handler pdCanceller = new Handler();
//            pdCanceller.postDelayed(progressRunnable, 2000);
//
//            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    progress.dismiss();
//                }
//            });
//        }

    }


    @Override
    public void onBackPressed() {
        String newContext = mContext.toString();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (newContext.contains("SingleLocation")) {
            super.onBackPressed();
        } else if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
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
        } else if (id == R.id.nav_home) {
            startActivity(new Intent(this, Home.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void apiCall() {

        String url = "https://api.foursquare.com/v2/venues/search?near=Dublin,IE&v=28012019&limit=1000&client_id=LIKFRNK34TNZQHOVJXSZEQNEFRGFS12VGLXRSHZBZKCG54XV&client_secret=EYNO0LDUNISNP2XBQIWZYP231NENGUA2GTYFFFHQAKGZGEFV";
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
                            locations = database.getReference("APILocations");
//                            allLocationList.add(new Location(locationId, locationName, locationLng, locationLat, mainAddress, iconUrl));
                            locations.child(locationId).setValue(new Location(locationId, locationName, locationLng, locationLat, mainAddress, iconUrl));
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
        mRequestQueue.add(jsonRequest);
    }



//    public void databasePull() {
//        mDatabase = FirebaseDatabase.getInstance().getReference("Locations");
//
//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                Location location = dataSnapshot.getValue(Location.class);
//                locationDBList.add(new Location(location.locationId, location.locationName, location.locationLong, location.locationLat, location.locationAddress, location.locationIcon, location.locationComments));
//                singleNews.add(new News(location.locationComments.get(0).commentName, location.locationName, location.locationComments.get(0).commentDate, location.locationIcon));
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                Location location = dataSnapshot.getValue(Location.class);
//                locationDBList.add(new Location(location.locationId, location.locationName, location.locationLong, location.locationLat, location.locationAddress, location.locationIcon, location.locationComments));
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                Toast.makeText(mContext, "Deletion Complete", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(mContext, "Failed to load comments.", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        mDatabase.addChildEventListener(childEventListener);
//
//    }

}
