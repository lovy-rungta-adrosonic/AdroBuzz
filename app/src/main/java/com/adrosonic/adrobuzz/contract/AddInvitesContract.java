package com.adrosonic.adrobuzz.contract;

import android.support.annotation.NonNull;

import com.adrosonic.adrobuzz.model.AddInvites;
import com.adrosonic.adrobuzz.sync.network.Resource;

import java.util.ArrayList;

/**
 * Created by Lovy on 04-05-2018.
 */

public interface AddInvitesContract {

    interface View {
        void setPresenter(Presenter presenter);

        void setLoadingIndicator(boolean active);

        void showLoadingError(String message);

        ArrayList<String> getListOfEmails();
    }

    interface Presenter {
        //        String getConfID();
//        String getVenue();
//        String getEmailID();
//        String getUserName();
//        String getDateTime(int index);
        void addInvites();
    }

    interface UseCase {
        void addInvites(ArrayList<String> request, @NonNull Completion completion);

        interface Completion {
            void didReceiveResource(Resource<AddInvites> resource);
        }
    }
}
