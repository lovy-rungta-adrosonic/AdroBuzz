package com.adrosonic.adrobuzz.contract;

import android.support.annotation.NonNull;

import com.adrosonic.adrobuzz.model.StartConf.StartConf;
import com.adrosonic.adrobuzz.sync.network.Resource;

/**
 * Created by Lovy on 20-04-2018.
 */

public interface StartConferenceContract {

    interface View{
        void setLoadingIndicator(boolean active);
        void showLoadingError(String message);
        void conferenceStarted();

    }

    interface  Presenter{
        String getConfID();
        String getVenue();
        String getEmailID();
        String getUserName();
        String getDateTime(int index);
        void startConference();
    }

    interface UseCase{
        void startConference(@NonNull Completion completion);

        interface Completion {
            void didReceiveResource(Resource<StartConf> resource);
        }
    }
}
