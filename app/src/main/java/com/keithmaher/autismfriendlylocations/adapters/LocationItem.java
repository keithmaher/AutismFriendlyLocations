package com.keithmaher.autismfriendlylocations.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.squareup.picasso.Picasso;

public class LocationItem {

    View view;
    private ImageView image;

    public LocationItem(Context context, ViewGroup parent, Location location)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.locationrow, parent, false);
        view.setTag(location.locationId);

        updateControls(location);
    }

    private void updateControls(Location location) {
        ((TextView) view.findViewById(R.id.newsUserName)).setText(location.locationName);
        ((TextView) view.findViewById(R.id.newsUserDate)).setText(location.locationAddress);
        image = view.findViewById(R.id.newsUserImg);
        Picasso.get().load(location.locationIcon).into(image);
    }
}
