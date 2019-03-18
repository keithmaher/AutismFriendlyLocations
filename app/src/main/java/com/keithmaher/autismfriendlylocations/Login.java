package com.keithmaher.autismfriendlylocations;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class Login extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 0;
    static EditText userEmail;
    static EditText userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginbasefragment);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        SpringDotsIndicator springDotsIndicator = findViewById(R.id.spring_dots_indicator);
        springDotsIndicator.setViewPager(mViewPager);

        mGoogleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Error Signing in to Google " + connectionResult, Toast.LENGTH_LONG).show();
    }


    public static class PlaceholderFragment extends Fragment{

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = null;
            int a = 0;
            if (getArguments() != null) {
                a = getArguments().getInt(ARG_SECTION_NUMBER);
            }

            if (a == 1){

                rootView = inflater.inflate(R.layout.loginfragmentone, container, false);

            }

            if (a == 2){

                rootView = inflater.inflate(R.layout.loginfragmenttwo, container, false);

            }

            if (a == 3) {

                rootView = inflater.inflate(R.layout.loginfragmentthree, container, false);
                userEmail = rootView.findViewById(R.id.load3Email);
                userPassword = rootView.findViewById(R.id.load3Password);
                rootView.findViewById(R.id.load3LoginButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginButton();
                    }
                });
                rootView.findViewById(R.id.google_sigin_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signIn();
                    }
                });
                rootView.findViewById(R.id.google_disconnect_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        revokeAccess();
                    }
                });
                rootView.findViewById(R.id.load3SkipButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        v = getLayoutInflater().inflate(R.layout.registeralertbox, null);
                        alertDialog.setView(v);
                        final EditText regEmail = v.findViewById(R.id.registerEmail);
                        final EditText regPassword = v.findViewById(R.id.registerPassword);
                        alertDialog.setTitle("Registration")
                                .setMessage("Please complete the following"
                                        + "\n\n"
                                        + "Email & Password")
                                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (regEmail.getText().length() == 0 || regPassword.getText().length() == 0){
                                            Toast.makeText(getContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
                                        }else {
                                            firebaseAuth.createUserWithEmailAndPassword(regEmail.getText().toString(), regPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(getContext(), "Registration Complete" , Toast.LENGTH_SHORT).show();
                                                                userEmail.setText(regEmail.getText());
                                                                userPassword.setText(regPassword.getText());
                                                            }else{
                                                                Toast.makeText(getContext(), "Sorry, Something went wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        alertDialog.setView(v);
                        AlertDialog dialog = alertDialog.create();
                        dialog.show();
                    }
                });

            }

            return rootView;
        }

        public void loginButton(){
            if (userEmail.getText().length() == 0 || userPassword.getText().length() == 0){
                Toast.makeText(getContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
            }else {
                firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(), "Signing in" , Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), BaseActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }else{
                                    Toast.makeText(getContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

        private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        private void handleSignInResult(GoogleSignInResult result) {
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                googleName = acct.getDisplayName();
                googleToken = acct.getId();
                signedIn = true;
                googleMail = acct.getEmail();
                if(acct.getPhotoUrl() == null)
                    ; //New Account may not have Google+ photo
                else googlePhotoURL = acct.getPhotoUrl().toString();

                firebaseAuthWithGoogle(acct);
            }
        }

        public void firebaseAuthWithGoogle(GoogleSignInAccount account) {

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                firebaseUser = firebaseAuth.getCurrentUser();
                                Toast.makeText(getContext(), "Signing in with " + googleMail, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), BaseActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }

        @Override
        public void onStart() {
            super.onStart();

            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }

        private void revokeAccess() {
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
            new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
