package com.adrosonic.adrobuzz.contract;

import android.support.annotation.NonNull;

import com.adrosonic.adrobuzz.model.CreateConf.CreateConfRequest;
import com.adrosonic.adrobuzz.model.ServiceResponse;
import com.adrosonic.adrobuzz.sync.network.Resource;

/**
 * Created by Lovy on 20-04-2018.
 */

public interface CreateConferenceContract {

    interface View{
        void setLoadingIndicator(boolean active);
        void showLoadingError(String message);
        void showConfID();
        CreateConfRequest getConferenceParameters();
    }

    interface  Presenter{
        void fetchConferenceID();
    }

    interface UseCase{
        void fetchConferenceID(CreateConfRequest request,@NonNull Completion completion);

        interface Completion {
            void didReceiveResource(Resource<ServiceResponse> resource);
        }
    }
}
