package com.keithmaher.autismfriendlylocations.adapters;

import android.widget.Filter;

import com.keithmaher.autismfriendlylocations.models.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationFilter extends Filter {
    public List<Location> originalLocationList;
    public String filterText;
    public LocationListAdapter adapter;

    public LocationFilter(List<Location> originalLocationList, String filterText,
                          LocationListAdapter adapter) {
        super();
        this.originalLocationList = originalLocationList;
        this.filterText = filterText;
        this.adapter = adapter;
    }

    public void setFilter(String filterText) {
        this.filterText = filterText;
    }

    @Override
    protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();

        List<Location> newLocation;
        String locationName;

        if (prefix == null || prefix.length() == 0) {
            newLocation = new ArrayList<>();
            if (filterText.equals("all")) {
                results.values = originalLocationList;
                results.count = originalLocationList.size();
            } else {
                if (filterText.equals("favourites")) {
                    for (Location c : originalLocationList)
                        if (c.locationFavourites)
                            newLocation.add(c);
                }
                results.values = newLocation;
                results.count = newLocation.size();
            }
        } else {
            String prefixString = prefix.toString().toLowerCase();
            newLocation = new ArrayList<>();

            for (Location c : originalLocationList) {
                locationName = c.locationName.toLowerCase();
                if (locationName.contains(prefixString)) {
                    if (filterText.equals("all")) {
                        newLocation.add(c);
                    } else if (c.locationFavourites) {
                        newLocation.add(c);
                    }
                }
            }
            results.values = newLocation;
            results.count = newLocation.size();
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence prefix, FilterResults results) {

        adapter.locationList = (ArrayList<Location>) results.values;

        if (results.count >= 0)
            adapter.notifyDataSetChanged();
        else {
            adapter.notifyDataSetInvalidated();
            adapter.locationList = originalLocationList;
        }
    }
}