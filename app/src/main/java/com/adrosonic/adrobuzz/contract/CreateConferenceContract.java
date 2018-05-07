package com.adrosonic.adrobuzz.contract;

import android.support.annotation.NonNull;

import com.adrosonic.adrobuzz.model.CreateConf;
import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.adrosonic.adrobuzz.model.Hero;
import com.adrosonic.adrobuzz.sync.network.Resource;

import java.util.List;

/**
 * Created by Lovy on 20-04-2018.
 */

public interface CreateConferenceContract {

    interface View{
        void setPresenter(Presenter presenter);
        void setLoadingIndicator(boolean active);
        void showLoadingError(String message);
        void showConfID(CreateConf confID);
        CreateConfRequest getConferenceParameters();
    }

    interface  Presenter{
        void fetchConferenceID();
    }

    interface UseCase{
        void fetchConferenceID(CreateConfRequest request,@NonNull Completion completion);

        interface Completion {
            void didReceiveResource(Resource<CreateConf> resource);
        }
    }
}
