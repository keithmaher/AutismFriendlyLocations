package com.keithmaher.autismfriendlylocations;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.keithmaher.autismfriendlylocations.fragments.BaseFragment;

import de.hdodenhof.circleimageview.CircleImageView;
import mumayank.com.airlocationlibrary.AirLocation;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    public DrawerLayout drawer;
    private static RequestQueue mRequestQueue;
    public GoogleSignInOptions mGoogleSignInOptions;
    public static GoogleApiClient mGoogleApiClient;
    public CircleImageView googlePhoto;
    public static Bitmap googlePhoto1;
    static FirebaseAuth firebaseAuth;
    static FirebaseUser firebaseUser;
    public String GPSLocationLAT;
    public String GPSLocationLNG;
    private AirLocation airLocation;
    private static final int REQUEST_LOCATION = 123;
    public SharedPreferences mPreference;
    public SharedPreferences.Editor mEditor;
    public NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseFragment.newsFragment(this);

        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getGPS();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(BaseActivity.this, REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mRequestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activitymenu);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View userView = navigationView.getHeaderView(0);

        if (firebaseUser != null) {

            googlePhoto = userView.findViewById(R.id.profile_image);
            if (firebaseUser.getPhotoUrl() != null) {
                getGooglePhoto(firebaseUser.getPhotoUrl().toString(), googlePhoto);
            }
            if (firebaseUser.getDisplayName() != null) {
                TextView googleName = userView.findViewById(R.id.navName);
                googleName.setText(firebaseUser.getDisplayName());
            }

            TextView googleMail = userView.findViewById(R.id.navEmail);
            googleMail.setText(firebaseUser.getEmail());
        }
    }

    public void getGPS(){
        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NotNull Location location) {
                GPSLocationLAT = String.valueOf(location.getLatitude());
                GPSLocationLNG = String.valueOf(location.getLongitude());
            }

            @Override
            public void onFailed(@NotNull AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
            }
        });
    }

    // override and call airLocation object's method by the same name
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    // override and call airLocation object's method by the same name
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void onBackPressed() {

        String singleLocation = getSupportFragmentManager().getFragments().toString();
        String loginActivity = getIntent().toString();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(singleLocation.contains("Single") || loginActivity.contains("Login") || singleLocation.contains("UserComments")) {
            super.onBackPressed();
        }else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(!drawer.isDrawerOpen(GravityCompat.START)){
            drawer.openDrawer(GravityCompat.START);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            BaseFragment.databaseLocationFragment(this);
        } else if (id == R.id.nav_add) {
            BaseFragment.apiLocationFragment(this);
        } else if (id == R.id.nav_home) {
            BaseFragment.newsFragment(this);
        }else if (id == R.id.menu_signout){
            menuSignOut();
        }else if (id == R.id.nav_my_search){
            BaseFragment.userLocationFragment(this);
        }else if (id == R.id.searchSettings){
            BaseFragment.searchFragment(this);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//     [START signOut]
    public void menuSignOut() {

        new AlertDialog.Builder(this)
                .setTitle("Don't Go")
                .setMessage("Please\nCome back soon")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void logout() {
        mGoogleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();

        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                if(mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(BaseActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Toast.makeText(BaseActivity.this, "Google API Client Connection Suspended",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void getGooglePhoto(final String url, final CircleImageView googlePhoto) {
        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        googlePhoto1 = response;
                        googlePhoto.setImageBitmap(BaseActivity.googlePhoto1);
//                        uploadImage(Uri.parse(url));
                    }
                }, 0, 0, Bitmap.Config.ARGB_8888,

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Something went wrong!");
                        error.printStackTrace();
                    }
                });
        // Add the request to the queue
        BaseActivity.add(imgRequest);
    }

    public static <T> void add(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

}
