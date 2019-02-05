package com.keithmaher.autismfriendlylocations;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.keithmaher.autismfriendlylocations.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Home extends BaseActivity {

    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_alllocations, null, false);
        drawer.addView(contentView, 0);
        setTitle("News Feed");
        mRequestQueue = Volley.newRequestQueue(this);
        if(locationList.isEmpty()) api();


//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("Locations");
//
//        myRef.setValue("Testing");
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Toast.makeText(Home.this, value, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Toast.makeText(Home.this, "Failed", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }



    @Override
    protected void onResume() {
        super.onResume();

//        locationFragment = LocationFragment.newInstance(); //get a new Fragment instance
//        getFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, locationFragment)
//                .commit();// add it to the current activity

    }

    public void api() {

        String url = "https://api.foursquare.com/v2/venues/search?near=Clonmel,IE&v=28012019&limit=10&client_id=LIKFRNK34TNZQHOVJXSZEQNEFRGFS12VGLXRSHZBZKCG54XV&client_secret=EYNO0LDUNISNP2XBQIWZYP231NENGUA2GTYFFFHQAKGZGEFV";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    //loop trough the data array to pull out the needed information
                    JSONObject locationInformation = response.getJSONObject("response");
                    JSONArray venueInformation = locationInformation.getJSONArray("venues");

                    for (int i = 0; i < venueInformation.length(); i++) {
                        JSONObject locationObject = venueInformation.getJSONObject(i);
                        locationName = locationObject.optString("name");
                        locationId = locationObject.optString("id");
                        JSONObject address = locationObject.getJSONObject("location");
                        locationRoad = address.optString("address");
                        locationCity = address.optString("city");
                        locationState = address.optString("state");
                        locationLat = Double.parseDouble(address.optString("lat"));
                        locationLng = Double.parseDouble(address.optString("lng"));
                        String mainAddress = locationRoad+"\n"+locationCity+"\n"+locationState;

                        locationList.add(new Location(locationId, locationName, locationLng, locationLat, mainAddress, 0));
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
