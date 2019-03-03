package com.keithmaher.autismfriendlylocations.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.keithmaher.autismfriendlylocations.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllDBSearchFragment extends AllDBLocationFragment implements AdapterView.OnItemSelectedListener {

    String selected;
    SearchView searchView;

    public AllDBSearchFragment() {
        // Required empty public constructor
    }

    public static AllDBSearchFragment newInstance() {
        AllDBSearchFragment fragment = new AllDBSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(activity, R.array.locationFilter, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchView = activity.findViewById(R.id.searchView);
        searchView.setQueryHint("AddLocation Locations");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                locationFilter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                locationFilter.filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context c) { super.onAttach(c); }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void checkSelected(String selected)
    {
        if (selected != null) {
            if (selected.equals("Refine AddLocation")) {
                locationFilter.setFilter("all");
            }else if (selected.equals("All Types")){
                locationFilter.setFilter("all");
            }else if (selected.equals("Coffee Shop")){
                locationFilter.setFilter("all");
            }else if (selected.equals("Shopping")){
                locationFilter.setFilter("all");
            }

            String filterText = ((SearchView)activity.findViewById(R.id.searchView)).getQuery().toString();

            if(filterText.length() > 0)
                locationFilter.filter(filterText);
            else
                locationFilter.filter("");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected = parent.getItemAtPosition(position).toString();
        checkSelected(selected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }



}
