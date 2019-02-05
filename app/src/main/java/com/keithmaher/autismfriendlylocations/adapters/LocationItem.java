package com.keithmaher.autismfriendlylocations.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Location;

public class LocationItem {

    View view;

    public LocationItem(Context context, ViewGroup parent, Location location)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.locationrow, parent, false);
        view.setTag(location.locationId);

        updateControls(location);
    }

    private void updateControls(Location location) {
        ((TextView) view.findViewById(R.id.rowLocationName)).setText(location.locationName);
        ((TextView) view.findViewById(R.id.rowLocationAddress)).setText(location.locationAddress);
    }
}
