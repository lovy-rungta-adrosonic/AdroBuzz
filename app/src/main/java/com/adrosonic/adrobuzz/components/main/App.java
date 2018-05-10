package com.adrosonic.adrobuzz.components.main;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.components.CreateConference.StartConferenceActivity;
import com.adrosonic.adrobuzz.components.Speech2Text.SpeechToTextActivity;
import com.adrosonic.adrobuzz.contract.SpeechToTextContract;
import com.adrosonic.adrobuzz.di.AppComponent;
import com.adrosonic.adrobuzz.di.AppModule;
import com.adrosonic.adrobuzz.di.DaggerAppComponent;
import com.adrosonic.adrobuzz.interactor.SpeechToTextInteractor;
import com.adrosonic.adrobuzz.model.ConferenceStatus;
import com.adrosonic.adrobuzz.model.Data;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.api.SyncModule;
import com.adrosonic.adrobuzz.sync.network.Environment;
import com.adrosonic.adrobuzz.sync.network.Resource;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import retrofit2.Retrofit;

import static com.adrosonic.adrobuzz.sync.network.Environment.DEV;

/**
 * Created by Lovy on 26-04-2018.
 */

public class App extends Application {

        private AppComponent appComponent;

    @Inject
    Retrofit retrofit;

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

        appComponent.inject(this);
        final Service service = retrofit.create(Service.class);

        String confId = PreferenceManager.getInstance(getApplicationContext()).getConfID();
        if(!confId.isEmpty()){
            SpeechToTextInteractor mInteractor = new SpeechToTextInteractor(getApplicationContext(),service);
            mInteractor .getConferenceStatus(new SpeechToTextContract.UseCase.Completion() {
                @Override
                public void didReceiveResource(Resource<ConferenceStatus> resource) {

                    switch (resource.status) {
                        case LOADING:
                            break;
                        case ERROR:
                            break;
                        case SUCCESS:
                            Data data = resource.data.getData();
                            if(data!=null){
                                switch (data.getId()) {
                                    case 1:
                                        Log.e("APP","status is 1");
                                        PreferenceManager.getInstance(getApplicationContext()).setConferenceStatus(1);
                                        break;
                                    case 2:
                                        Log.e("APP","status is 2");
                                        PreferenceManager.getInstance(getApplicationContext()).setConferenceStatus(2);
                                        break;
                                    case 3:
                                        Log.e("APP","status is 3");
                                        PreferenceManager.getInstance(getApplicationContext()).setConferenceStatus(3);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}
