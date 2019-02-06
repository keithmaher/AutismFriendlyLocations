package com.keithmaher.autismfriendlylocations.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Location;

import java.util.List;

public class LocationSearchAdapter extends ArrayAdapter<Location> {

    private Context context;
    public List<Location> locationSearchList;

    public LocationSearchAdapter(Context context, List<Location> locationSearchList)
    {
        super(context, R.layout.locationrow, locationSearchList);

        this.context = context;
        this.locationSearchList = locationSearchList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LocationItem item = new LocationItem(context, parent, locationSearchList.get(position));
        return item.view;
    }

    @Override
    public int getCount()
    {
        return locationSearchList.size();
    }

    @Override
    public Location getItem(int position) {
        return locationSearchList.get(position);
    }
}
