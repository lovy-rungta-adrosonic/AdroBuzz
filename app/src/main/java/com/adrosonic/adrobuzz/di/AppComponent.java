package com.adrosonic.adrobuzz.di;

import com.adrosonic.adrobuzz.components.createConference.AddInvitesActivity;
import com.adrosonic.adrobuzz.components.createConference.AddInvitesPresenter;
import com.adrosonic.adrobuzz.components.createConference.CreateConference;
import com.adrosonic.adrobuzz.components.createConference.CreateConferencePresenter;
import com.adrosonic.adrobuzz.components.createConference.StartConferenceActivity;
import com.adrosonic.adrobuzz.components.createConference.StartConferencePresenter;
import com.adrosonic.adrobuzz.components.joinConference.JoinConference;
import com.adrosonic.adrobuzz.components.joinConference.JoinConferencePresenter;
import com.adrosonic.adrobuzz.components.speech2Text.SpeechToTextActivity;
import com.adrosonic.adrobuzz.components.speech2Text.SpeechToTextPresenter;
import com.adrosonic.adrobuzz.components.main.App;
import com.adrosonic.adrobuzz.components.main.MainActivity;
import com.adrosonic.adrobuzz.sync.api.SyncModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lovy on 26-04-2018.
 */

@Singleton
@Component(modules = {
        AppModule.class,
        SyncModule.class})
public interface AppComponent {

    void inject(App app);

    void inject(MainActivity activity);

    void inject(CreateConference activity);

    void inject(CreateConferencePresenter presenter);

    void inject(StartConferenceActivity activity);

    void inject(StartConferencePresenter presenter);

    void inject(AddInvitesActivity activity);

    void inject(AddInvitesPresenter presenter);

    void inject(SpeechToTextActivity activity);

    void inject(SpeechToTextPresenter presenter);

    void inject(JoinConference activity);

    void inject(JoinConferencePresenter presenter);

}