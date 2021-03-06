package com.keithmaher.autismfriendlylocations.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.keithmaher.autismfriendlylocations.BaseActivity;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.LocationAdapterView;
import com.keithmaher.autismfriendlylocations.models.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

public class LocationAPIFragment extends BaseFragment{

    LocationAdapterView adapter;
    GifImageView loading;
    public static String locationName;
    public static String locationId;
    public static String locationRoad;
    public static String locationCity;
    public static String locationState;
    public static double locationLng;
    public static double locationLat;
    BaseActivity baseActivity;
    TextView noLocation;
    RequestQueue mRequestQueue;


    public LocationAPIFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        baseActivity = (BaseActivity)getActivity();

        baseActivity.getGPS();

        mRequestQueue = Volley.newRequestQueue(getContext());

        getActivity().setTitle("Search Locally");

        if (baseActivity.GPSLocationLNG != null) {

            android.location.Location MylocationA = new android.location.Location("Location A");

            MylocationA.setLatitude(Double.parseDouble(baseActivity.GPSLocationLAT));
            MylocationA.setLongitude(Double.parseDouble(baseActivity.GPSLocationLNG));

            adapter = new LocationAdapterView(apiLocationList, getActivity(), MylocationA);
        }else{
            adapter = new LocationAdapterView(apiLocationList, getActivity());
        }

        View view = inflater.inflate(R.layout.recyclelistfragment, container, false);
        loading = view.findViewById(R.id.loadingGif2);
        noLocation = view.findViewById(R.id.noLocationText);
        noLocation.setVisibility(View.GONE);
        RecyclerView mRecycler = view.findViewById(R.id.recycler);
        mRecycler.setAdapter(adapter);

        locationAPI(loading);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);


        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                loading.setVisibility(View.GONE);
                if (apiLocationList.isEmpty()) {
                    noLocation.setVisibility(View.VISIBLE);
                }
                noLocation.setText("No locations\nExpand your search");
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 5000);

        return view;
    }

    public void locationAPI(final GifImageView loading) {

        apiLocationList.clear();
        String url;

        Boolean prefCheck = baseActivity.mPreference.getBoolean("location", true);
        String prefName = baseActivity.mPreference.getString("name", "");
        String prefQuery = baseActivity.mPreference.getString("query", "");
        int prefLimit = baseActivity.mPreference.getInt("limit", 5);
        int prefRadius = baseActivity.mPreference.getInt("radius", 5);
        prefRadius = prefRadius*1000;
        if (!prefName.isEmpty()){
            prefName = prefName+",IE";
        }

        if (prefCheck){
            url = getString(R.string.url_base)+"?ll="+baseActivity.GPSLocationLAT+","+baseActivity.GPSLocationLNG+"&radius="+prefRadius+"&query="+prefQuery+"&v=28012019&limit="+prefLimit+"&client_id="+getString(R.string.api_client)+"&client_secret="+getString(R.string.api_secret)+"";
        }else{
            url = getString(R.string.url_base)+"?near="+prefName+"&query="+prefQuery+"&radius="+prefRadius+"&v=28012019&limit="+prefLimit+"&client_id="+getString(R.string.api_client)+"&client_secret="+getString(R.string.api_secret)+"";
        }

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
                            apiLocationList.add(new Location(locationId, locationName, locationLng, locationLat, mainAddress, iconUrl));
                            adapter.notifyDataSetChanged();
                            loading.setVisibility(View.GONE);
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
}
