package com.adrosonic.adrobuzz.contract;

import android.support.annotation.NonNull;

import com.adrosonic.adrobuzz.model.ServiceResponse;
import com.adrosonic.adrobuzz.sync.network.Resource;

import java.util.ArrayList;

/**
 * Created by Lovy on 04-05-2018.
 */

public interface AddInvitesContract {

    interface View {

        void setLoadingIndicator(boolean active);
        void showLoadingError(String message);
        void finishActivity();

        ArrayList<String> getListOfEmails();
    }

    interface Presenter {
        void addInvites();
    }

    interface UseCase {
        void addInvites(ArrayList<String> request, @NonNull Completion completion);

        interface Completion {
            void didReceiveResource(Resource<ServiceResponse> resource);
        }
    }
}
