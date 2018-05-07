package com.adrosonic.adrobuzz.di;

import com.adrosonic.adrobuzz.components.CreateConference.AddInvitesActivity;
import com.adrosonic.adrobuzz.components.CreateConference.AddInvitesPresenter;
import com.adrosonic.adrobuzz.components.CreateConference.CreateConference;
import com.adrosonic.adrobuzz.components.CreateConference.CreateConferencePresenter;
import com.adrosonic.adrobuzz.components.CreateConference.StartConferenceActivity;
import com.adrosonic.adrobuzz.components.CreateConference.StartConferencePresenter;
import com.adrosonic.adrobuzz.components.main.MainActivity;
import com.adrosonic.adrobuzz.sync.api.SyncModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lovy on 26-04-2018.
 */

@Singleton
@Component(modules = {
//        AndroidSupportInjectionModule.class,
//        CreateConference.class,
        AppModule.class,
        SyncModule.class})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(CreateConference activity);
//
    void inject(CreateConferencePresenter presenter);

    void inject(StartConferenceActivity activity);
    //
    void inject(StartConferencePresenter presenter);

    void inject(AddInvitesActivity activity);
    //
    void inject(AddInvitesPresenter presenter);



}