package com.keithmaher.autismfriendlylocations.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.adapters.ViewPagerAdapter;


public class APILocationFragment extends BaseFragment {

    public static APILocationFragment newInstance() {
        APILocationFragment fragment = new APILocationFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.viewpagerfragment, container, false);
        ViewPager viewPager;
        viewPager = view.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(locationFragment());
        viewPager.setAdapter(adapter);

        return view;
    }

}
