package com.adrosonic.adrobuzz.components.main;

import android.app.Activity;
import android.app.Application;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.di.AppComponent;
import com.adrosonic.adrobuzz.di.AppModule;
import com.adrosonic.adrobuzz.di.DaggerAppComponent;
import com.adrosonic.adrobuzz.sync.api.SyncModule;
import com.adrosonic.adrobuzz.sync.network.Environment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

import static com.adrosonic.adrobuzz.sync.network.Environment.DEV;

/**
 * Created by Lovy on 26-04-2018.
 */

public class App extends Application {

        private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final SyncModule syncModule = new SyncModule(DEV);
        syncModule.provideEnvironment();
        final AppModule appModule = new AppModule(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .syncModule(syncModule)
                .build();
//        appComponent.inject(this);

//        setURL(DEV);

    }

    private void setURL(Environment environment) {
        switch (environment) {
            case DEV:
                PreferenceManager.getInstance(getApplicationContext()).setBaseURL("http://34.231.184.70/Conference/services/conferenceAPI/");
                return;
            case TEST:
                PreferenceManager.getInstance(getApplicationContext()).setBaseURL("http://34.249.194.50/TEG/");
                return;
            case UAT:
                PreferenceManager.getInstance(getApplicationContext()).setBaseURL("https://www.bestforexrate.com/TEGAPI/");
                return;
            case LIVE:
                PreferenceManager.getInstance(getApplicationContext()).setBaseURL( "https://www.bestforexrate.co.uk/TEGAPI/");
                return;
            default:
                return;
        }
    }
}
