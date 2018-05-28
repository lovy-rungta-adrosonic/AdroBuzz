package com.adrosonic.adrobuzz.contract;

import android.support.annotation.NonNull;

import com.adrosonic.adrobuzz.model.ConfAttendees.ConfAttendees;
import com.adrosonic.adrobuzz.model.ConfStatus.ConferenceStatus;
import com.adrosonic.adrobuzz.model.ServiceResponse;
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
//        void showAttendeeList();
        void leaveConfSuccess();
        void showMessage(String message);
    }

    interface  Presenter{
        String getConfSubject();
        void endConference();
        void getConferenceStatus();
        void getConferenceStatusLogOut();
//        void getConferenceAttendees();
        void leaveConference();
        void sendMailAdmin();
        void sendMailNonAdmin();
    }

    interface UseCase{
        void endConference(@NonNull EndConfCompletion completion);
        void getConferenceStatus(@NonNull Completion completion);
//        void getConferenceAttendees(@NonNull ConfAttendeesCompletion completion);
        void leaveConference(@NonNull LeaveConfCompletion completion);
        void sendMailAdmin(@NonNull SendMailCompletion completion);
        void sendMailNonAdmin(@NonNull SendMailCompletion completion);

        interface Completion {
            void didReceiveResource(Resource<ConferenceStatus> resource);
        }

        interface EndConfCompletion {
            void didReceiveResource(Resource<ServiceResponse> resource);
        }

//        interface ConfAttendeesCompletion {
//            void didReceiveResource(Resource<ConfAttendees> resource);
//        }

        interface LeaveConfCompletion {
            void didReceiveResource(Resource<ServiceResponse> resource);
        }

        interface SendMailCompletion {
            void didReceiveResource(Resource<ServiceResponse> resource);
        }


    }
}
