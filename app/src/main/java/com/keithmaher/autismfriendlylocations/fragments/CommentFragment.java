package com.keithmaher.autismfriendlylocations.fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.keithmaher.autismfriendlylocations.activities.BaseActivity;
import com.keithmaher.autismfriendlylocations.activities.SearchDBLocations;
import com.keithmaher.autismfriendlylocations.activities.SingleLocation;
import com.keithmaher.autismfriendlylocations.adapters.CommentListAdapter;
import com.keithmaher.autismfriendlylocations.models.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.keithmaher.autismfriendlylocations.activities.BaseActivity.locationComments;
import static com.keithmaher.autismfriendlylocations.activities.BaseActivity.singleComment;

public class CommentFragment extends ListFragment implements View.OnClickListener, AbsListView.MultiChoiceModeListener {

    public BaseActivity activity;
    public static CommentListAdapter listAdapter;
    public ListView listView;

    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment newInstance() {
        CommentFragment fragment = new CommentFragment();
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
        listAdapter = new CommentListAdapter(activity,  singleComment);
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

        new AlertDialog.Builder(activity)
                .setTitle(singleComment.get(position).commentName)
                .setMessage(singleComment.get(position).commentMain
                        + "\n\n"
                        + "Date added: "+singleComment.get(position).commentDate)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
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