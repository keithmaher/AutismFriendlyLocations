package com.keithmaher.autismfriendlylocations.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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
import com.keithmaher.autismfriendlylocations.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Home extends BaseActivity {

    RequestQueue mRequestQueue;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Location aLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_alllocations, null, false);
        drawer.addView(contentView, 0);
        setTitle("News Feed");
        mRequestQueue = Volley.newRequestQueue(this);
        if(locationList.isEmpty()) api();
        if(locationSearchList.isEmpty()) firebase();

    }

    private void firebase() {

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
                int likes = Integer.parseInt(dataSnapshot.child("locationLikes").getValue().toString());
                String icon = dataSnapshot.child("locationIcon").getValue().toString();

                locationSearchList.add(new Location(id, name, lng, lat, address, likes, icon));

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


    @Override
    protected void onResume() {
        super.onResume();

//        allLocationFragment = AllLocationFragment.newInstance(); //get a new Fragment instance
//        getFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, allLocationFragment)
//                .commit();// add it to the current activity

    }

    public void api() {

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
                            locationList.add(new Location(locationId, locationName, locationLng, locationLat, mainAddress, 0, iconUrl));
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

}
