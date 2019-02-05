package com.keithmaher.autismfriendlylocations.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Location;

import java.util.List;

public class LocationListAdapter extends ArrayAdapter<Location> {

    private Context context;
    public List<Location> locationList;

    public LocationListAdapter(Context context, List<Location> locationList)
    {
        super(context, R.layout.locationrow, locationList);

        this.context = context;
        this.locationList = locationList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LocationItem item = new LocationItem(context, parent, locationList.get(position));
        return item.view;
    }

    @Override
    public int getCount()
    {
        return locationList.size();
    }

    @Override
    public Location getItem(int position) {
        return locationList.get(position);
    }
}
