package com.keithmaher.autismfriendlylocations.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.keithmaher.autismfriendlylocations.BaseActivity;
import com.keithmaher.autismfriendlylocations.R;

public class MainSearchFragment extends BaseFragment{

    BaseActivity baseActivity;
    Switch userLocation;
    EditText townName;
    Button saveTownButton;
    Button deleteTownButton;
    TextView savedTown;
    EditText query;
    Button saveQueryButton;
    Button deleteQuery;
    TextView savedQuery;

    public MainSearchFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        baseActivity = (BaseActivity)getActivity();
        baseActivity.navigationView.setCheckedItem(R.id.searchSettings);

        getActivity().setTitle("Search Settings");

        View view = inflater.inflate(R.layout.searchfragment, container, false);

        userLocation = view.findViewById(R.id.switch1);
        townName = view.findViewById(R.id.townInput);
        savedTown = view.findViewById(R.id.savedTown);
        saveTownButton = view.findViewById(R.id.saveButton);
        deleteTownButton = view.findViewById(R.id.deleteButton);
        query = view.findViewById(R.id.queryInput);
        savedQuery = view.findViewById(R.id.savedQuery);
        saveQueryButton = view.findViewById(R.id.saveQuery);
        deleteQuery = view.findViewById(R.id.deleteQueryButton);
        SeekBar seekbar = view.findViewById(R.id.seekBar);
        SeekBar radiusseekbar = view.findViewById(R.id.seekBar2);

        checkPreferences();

        saveTownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = townName.getText().toString();
                if(input.isEmpty()){
                    Toast.makeText(getContext(), "Town needs an input", Toast.LENGTH_SHORT).show();
                }else {
                    baseActivity.mEditor = baseActivity.mPreference.edit();
                    baseActivity.mEditor.putString("name", input);
                    baseActivity.mEditor.commit();

                    savedTown.setText(input);
                    savedTown.setVisibility(View.VISIBLE);
                    deleteTownButton.setVisibility(View.VISIBLE);
                    townName.setText("");
                }
            }
        });

        saveQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = query.getText().toString();
                if(input.isEmpty()){
                    Toast.makeText(getContext(), "Query needs an input", Toast.LENGTH_SHORT).show();
                }else {
                    baseActivity.mEditor = baseActivity.mPreference.edit();
                    baseActivity.mEditor.putString("query", input);
                    baseActivity.mEditor.commit();

                    savedQuery.setText(input);
                    savedQuery.setVisibility(View.VISIBLE);
                    deleteQuery.setVisibility(View.VISIBLE);
                    query.setText("");
                }
            }
        });

        deleteQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.mEditor.remove("query").commit();
                Toast.makeText(getContext(), "Removing Query", Toast.LENGTH_SHORT).show();
                BaseFragment.searchFragment(getActivity());
            }
        });

        deleteTownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.mEditor.remove("name").commit();
                Toast.makeText(getContext(), "Removing Town", Toast.LENGTH_SHORT).show();
                BaseFragment.searchFragment(getActivity());
            }
        });


        userLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    townName.setVisibility(View.VISIBLE);
                    if (!savedTown.getText().toString().isEmpty()){
                        savedTown.setVisibility(View.VISIBLE);
                        deleteTownButton.setVisibility(View.VISIBLE);
                    }
                    saveTownButton.setVisibility(View.VISIBLE);
                    baseActivity.mEditor = baseActivity.mPreference.edit();
                    baseActivity.mEditor.putBoolean("location", false);
                    baseActivity.mEditor.commit();
                }else if (isChecked){
                    townName.setVisibility(View.GONE);
                    saveTownButton.setVisibility(View.GONE);
                    savedTown.setVisibility(View.GONE);
                    deleteTownButton.setVisibility(View.GONE);
                    baseActivity.mEditor = baseActivity.mPreference.edit();
                    baseActivity.mEditor.putBoolean("location", true);
                    baseActivity.mEditor.commit();
                }
            }
        });

        int limit = baseActivity.mPreference.getInt("limit", 5);
        baseActivity.mEditor = baseActivity.mPreference.edit();
        baseActivity.mEditor.putInt("limit", limit);
        baseActivity.mEditor.commit();
        seekbar.setProgress(limit);

        int radius = baseActivity.mPreference.getInt("radius", 5);
        baseActivity.mEditor = baseActivity.mPreference.edit();
        baseActivity.mEditor.putInt("radius", radius);
        baseActivity.mEditor.commit();
        radiusseekbar.setProgress(radius);

        final TextView result = view.findViewById(R.id.result);
        String progress = String.valueOf(seekbar.getProgress());
        result.setText(progress);

        final TextView result2 = view.findViewById(R.id.result2);
        String progress2 = String.valueOf(radiusseekbar.getProgress());
        result2.setText(progress2);

        // perform seek bar change listener event used for getting the progress value
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                result.setText(String.valueOf(progressChangedValue));
                baseActivity.mEditor = baseActivity.mPreference.edit();
                baseActivity.mEditor.putInt("limit", progressChangedValue);
                baseActivity.mEditor.commit();
            }
        });

        radiusseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                result2.setText(String.valueOf(progressChangedValue));
                baseActivity.mEditor = baseActivity.mPreference.edit();
                baseActivity.mEditor.putInt("radius", progressChangedValue);
                baseActivity.mEditor.commit();
            }
        });



        Button reset = view.findViewById(R.id.resetSettings);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.mEditor.clear();
                baseActivity.mEditor.commit();
                Toast.makeText(getContext(), "Resetting data", Toast.LENGTH_SHORT).show();
                BaseFragment.searchFragment(getActivity());
            }
        });





        return view;
    }

    private void checkPreferences() {

        Boolean check = baseActivity.mPreference.getBoolean("location", true);
        String locationName = baseActivity.mPreference.getString("name", "");
        String query = baseActivity.mPreference.getString("query", "");

        if (check){
            userLocation.setChecked(true);
            townName.setVisibility(View.GONE);
            saveTownButton.setVisibility(View.GONE);
            savedTown.setVisibility(View.GONE);
            deleteTownButton.setVisibility(View.GONE);
            if (query.isEmpty()){
                savedQuery.setVisibility(View.GONE);
                deleteQuery.setVisibility(View.GONE);
            }else{
                savedQuery.setVisibility(View.VISIBLE);
                savedQuery.setText(query);
                deleteQuery.setVisibility(View.VISIBLE);
            }
        }else{
            userLocation.setChecked(false);
            if (!locationName.isEmpty()){
                deleteTownButton.setVisibility(View.VISIBLE);
                savedTown.setVisibility(View.VISIBLE);
                savedTown.setText(locationName);
            }else{
                deleteTownButton.setVisibility(View.GONE);
                savedTown.setVisibility(View.GONE);
            }
            if (query.isEmpty()){
                savedQuery.setVisibility(View.GONE);
                deleteQuery.setVisibility(View.GONE);
            }else{
                savedQuery.setVisibility(View.VISIBLE);
                savedQuery.setText(query);
                deleteQuery.setVisibility(View.VISIBLE);
            }
            townName.setVisibility(View.VISIBLE);
            saveTownButton.setVisibility(View.VISIBLE);
        }

    }

}
