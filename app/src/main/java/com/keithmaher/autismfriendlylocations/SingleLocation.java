package com.keithmaher.autismfriendlylocations;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.keithmaher.autismfriendlylocations.models.Location;

public class SingleLocation extends BaseActivity {

    public Context context;
    public Location aLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_single_location, null, false);
        drawer.addView(contentView, 0);

        context = this;
        activityInfo = getIntent().getExtras();
        aLocation = getLocationObject(activityInfo.getString("locationId"));
        String name = aLocation.locationName;
        double lon = aLocation.locationLong;
        double lat = aLocation.locationLat;
        String rating = String.valueOf(aLocation.locationRating);

        String a = String.valueOf(lon);
        String b = String.valueOf(lat);

        String c = a +"\n"+b+"\n"+a;

        setTitle(name);

        ((TextView)findViewById(R.id.singleNameET)).setText(aLocation.locationName);
        ((TextView)findViewById(R.id.singleAddressET)).setText(c);
        ((TextView)findViewById(R.id.singleRatingET)).setText(rating);

    }

    private Location getLocationObject(String id) {

        for (Location c : locationList)
            if (c.locationId.equalsIgnoreCase(id))
                return c;

        return null;
    }

}
