package com.keithmaher.autismfriendlylocations.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.Utils.TinyDB;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.keithmaher.autismfriendlylocations.models.News;

import java.util.ArrayList;

public class BaseFragment extends Fragment {

    ArrayList<Location> apiLocationList = new ArrayList<>();
    ArrayList<Location> databaseLocationList = new ArrayList<>();
    ArrayList<Comment> locationCommentList = new ArrayList<>();
    ArrayList<Comment> userCommentList = new ArrayList<>();
    ArrayList<News> newsList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    public Location getLocationObject(Context context) {
        TinyDB tinydb = new TinyDB(context);
        return tinydb.getObject("locationObj", Location.class);
    }


    public static void singleLocationFragment(FragmentActivity activity) {
        SingleLocationFragment singleLocationFragment = new SingleLocationFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, singleLocationFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void apiLocationFragment(FragmentActivity activity) {
        APILocationFragment apiLocationFragment = new APILocationFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, apiLocationFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void userLocationFragment(FragmentActivity activity) {
        UserLocationFragment userLocationFragment = new UserLocationFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, userLocationFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void searchFragment(FragmentActivity activity) {
        SearchFragment searchFragment = new SearchFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, searchFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void databaseLocationFragment(FragmentActivity activity) {
        DatabaseLocationFragment databaseLocationFragment = new DatabaseLocationFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, databaseLocationFragment)
                .addToBackStack(null)
                .commit();
    }

    public static void newsFragment(FragmentActivity activity) {
        NewsMainFragment newsfragement = new NewsMainFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragment, newsfragement)
                .addToBackStack(null)
                .commit();
    }


    public static LocationAPIFragment locationFragment() {
        return new LocationAPIFragment();
    }

    public static LocationUserFragment userLocationFragment() {
        return new LocationUserFragment();
    }

    public static MainSearchFragment mainSearchFragment() {
        return new MainSearchFragment();
    }

    public static LocationDatabaseFragment locationDatabaseFragment() {
        return new LocationDatabaseFragment();
    }

    public static MainNewsFragment newsFragment() {
        return new MainNewsFragment();
    }

    public static LocationInformationFragment locationInfoFragment() {
        return new LocationInformationFragment();
    }

    public static CommentFragment commentFragment() {
        return new CommentFragment();
    }

}
