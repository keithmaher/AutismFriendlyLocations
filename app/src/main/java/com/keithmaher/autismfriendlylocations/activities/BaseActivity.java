package com.keithmaher.autismfriendlylocations.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.fragments.AllLocationFragment;
import com.keithmaher.autismfriendlylocations.fragments.AllDBLocationFragment;
import com.keithmaher.autismfriendlylocations.models.Location;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Bundle activityInfo; // Used for persistence (of sorts)
    public Bundle moreinfo; // Used for persistence (of sorts)
    public AllLocationFragment allLocationFragment;
    public AllDBLocationFragment allDBLocationFragment;
    public static List<Location> locationList = new ArrayList<>();
    public static List<Location> locationSearchList = new ArrayList<>();
    protected DrawerLayout drawer;
    public static String locationName;
    public static String locationId;
    public static String locationRoad;
    public static String locationCity;
    public static String locationState;
    public static double locationLng;
    public static double locationLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //startActivity(new Intent(this, Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            startActivity(new Intent(this, SearchDBLocations.class));
        } else if (id == R.id.nav_add) {
            startActivity(new Intent(this, AddLocation.class));
        }
//        else if (id == R.id.nav_login) {
//
//        } else if (id == R.id.nav_logout) {
//
//        }
        else if (id == R.id.nav_home) {
            startActivity(new Intent(this, Home.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void add(View v){
        //Toast.makeText(this, "Testing", Toast.LENGTH_SHORT).show();
    }

}
