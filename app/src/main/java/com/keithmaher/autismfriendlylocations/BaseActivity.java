package com.keithmaher.autismfriendlylocations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.keithmaher.autismfriendlylocations.fragments.BaseFragment;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static RequestQueue mRequestQueue;
    public GoogleSignInOptions mGoogleSignInOptions;
    public static GoogleApiClient mGoogleApiClient;

    public static boolean signedIn = false;
    public static String googleToken;
    public static String googleName;
    public static String googleMail;
    public static String googlePhotoURL;
    public CircleImageView googlePhoto;
    public static Bitmap googlePhoto1;
    static FirebaseAuth firebaseAuth;
    static FirebaseUser firebaseUser;
    public static FirebaseStorage storage;
    public static StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("userImages");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mRequestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activitymenu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Menu nav_Menu = navigationView.getMenu();

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


        setTheTitle("News Feed");

    }

    public void setTheTitle(String titleInput) {
        setTitle(titleInput);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

//        String singleLocation = getSupportFragmentManager().getFragments().toString();
//        if(singleLocation.contains("Single"))
//        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuAdd) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            Intent intent = new Intent(BaseActivity.this, BaseActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_signout){
            menuSignOut();
        }else if (id == R.id.nav_my_search){
            BaseFragment.userLocationFragment(this);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//     [START signOut]
    public void menuSignOut() {

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
    // [END signOut]

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

//    public static void uploadImage(Uri url) {
//
//        storageReference.putFile(url)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    }
//                });
//    }



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
