package com.keithmaher.autismfriendlylocations;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 4000; // 3 seconds


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Handler handler = new Handler();
        // run a thread after 3 seconds to start the home screen
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // make sure we close the splash screen so the user
                // won't come back to it if back key pressed
                finish();

                if (!mIsBackButtonPressed) {
                    // start the home screen if the back button wasn't pressed already
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    SplashScreen.this.startActivity(intent);
                }
            }
        }, SPLASH_DURATION); // time in milliseconds to delay call to run()
    }

    @Override
    public void onBackPressed() {
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();
    }

}