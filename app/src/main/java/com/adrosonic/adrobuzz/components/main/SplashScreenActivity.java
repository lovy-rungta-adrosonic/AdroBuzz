package com.adrosonic.adrobuzz.components.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.components.CreateConference.AddInvitesActivity;
import com.adrosonic.adrobuzz.components.CreateConference.CreateConference;
import com.adrosonic.adrobuzz.components.CreateConference.StartConferenceActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        setSupportActionBar(toolbar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isAdmin= PreferenceManager.getInstance(SplashScreenActivity.this).getIsAdmin();
                if(isAdmin){
                        startActivity(new Intent(SplashScreenActivity.this, StartConferenceActivity.class));
                }else {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
