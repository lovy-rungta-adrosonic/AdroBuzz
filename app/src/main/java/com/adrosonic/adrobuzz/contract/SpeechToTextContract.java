package com.adrosonic.adrobuzz.contract;

import android.support.annotation.NonNull;

import com.adrosonic.adrobuzz.model.ConfAttendees.ConfAttendees;
import com.adrosonic.adrobuzz.model.ConfStatus.ConferenceStatus;
import com.adrosonic.adrobuzz.sync.network.Resource;

/**
 * Created by Lovy on 09-05-2018.
 */

public interface SpeechToTextContract {

    interface View{
        void setLoadingIndicator(boolean active);
        void conferenceEnded();
        void showError(String message);
        void finalConfStatus(int status);
        void confStatus(int status);
        void showAttendeeList();
    }

    interface  Presenter{
        String getConfSubject();
        void endConference();
        void getConferenceStatus();
        void getConferenceStatusLogOut();
        void getConferenceAttendees();
    }

    interface UseCase{
        void endConference(@NonNull Completion completion);
        void getConferenceStatus(@NonNull Completion completion);
        void getConferenceAttendees(@NonNull ConfAttendeesCompletion completion);

        interface Completion {
            void didReceiveResource(Resource<ConferenceStatus> resource);
        }

        interface ConfAttendeesCompletion {
            void didReceiveResource(Resource<ConfAttendees> resource);
        }

    }
}
