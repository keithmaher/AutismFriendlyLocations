package com.keithmaher.autismfriendlylocations.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import com.keithmaher.autismfriendlylocations.activities.BaseActivity;
import com.keithmaher.autismfriendlylocations.activities.SingleLocation;
import com.keithmaher.autismfriendlylocations.adapters.LocationFilter;
import com.keithmaher.autismfriendlylocations.adapters.LocationListAdapter;
import static com.keithmaher.autismfriendlylocations.activities.BaseActivity.allLocationList;

public class AllLocationFragment extends ListFragment implements View.OnClickListener, AbsListView.MultiChoiceModeListener {

    public BaseActivity activity;
    public static LocationListAdapter listAdapter;
    public ListView listView;
    public LocationFilter locationFilter;

    public AllLocationFragment() {
        // Required empty public constructor
    }

    public static AllLocationFragment newInstance() {
        AllLocationFragment fragment = new AllLocationFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.activity = (BaseActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        listAdapter = new LocationListAdapter(activity,  allLocationList);
        locationFilter = new LocationFilter(allLocationList, "all", listAdapter);
        setListAdapter (listAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        listView = v.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Bundle activityInfo = new Bundle(); // Creates a new Bundle object
        Bundle moreinfo = new Bundle(); // Creates a new Bundle object
        activityInfo.putString("locationId", (String) v.getTag());
        moreinfo.putString("test", this.getActivity().getIntent().toString());
        Intent goEdit = new Intent(getActivity(), SingleLocation.class); // Creates a new Intent
        /* AddLocation the bundle to the intent here */
        goEdit.putExtras(activityInfo);
        goEdit.putExtras(moreinfo);
        getActivity().startActivity(goEdit);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}