package com.adrosonic.adrobuzz.contract;

import android.support.annotation.NonNull;

import com.adrosonic.adrobuzz.model.JoinConf.JoinConf;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConfRequest;
import com.adrosonic.adrobuzz.sync.network.Resource;

/**
 * Created by Lovy on 09-05-2018.
 */

public interface JoinConferenceContract {

    interface View {
        void setLoadingIndicator(boolean active);
        JoinConfRequest getParameters();
        String getConfId();
        void showLoadingError(String error);
        void joinedConference(JoinConf joinConf);
    }

    interface Presenter {
        void joinConference();
    }

    interface UseCase {
        void joinConference(String confId, JoinConfRequest request, @NonNull JoinConferenceContract.UseCase.Completion completion);

        interface Completion {
            void didReceiveResource(Resource<JoinConf> resource);
        }
    }
}
