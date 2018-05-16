package com.adrosonic.adrobuzz.components.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.components.createConference.StartConferenceActivity;
import com.adrosonic.adrobuzz.components.speech2Text.SpeechToTextActivity;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class SplashScreenActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final int status = PreferenceManager.getInstance(SplashScreenActivity.this).getConferenceStatus();
                switch (status) {
                    case 1:
                        boolean isAdmin = PreferenceManager.getInstance(SplashScreenActivity.this).getIsAdmin();
                        String id = PreferenceManager.getInstance(SplashScreenActivity.this).getConfID();
                        if (isAdmin && !id.equals("")) {
                            startActivity(new Intent(SplashScreenActivity.this, StartConferenceActivity.class));
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        }
                        break;

                    case 2:
                        startActivity(new Intent(SplashScreenActivity.this, SpeechToTextActivity.class));
                        break;

                    case 3:
                        startActivity(new Intent(SplashScreenActivity.this, SpeechToTextActivity.class));
                        break;

                    default:
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        break;
                }
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
