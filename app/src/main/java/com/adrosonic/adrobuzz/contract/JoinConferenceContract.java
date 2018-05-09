package com.adrosonic.adrobuzz.contract;

import android.support.annotation.NonNull;

import com.adrosonic.adrobuzz.model.APIResponse;
import com.adrosonic.adrobuzz.model.AddInvites;
import com.adrosonic.adrobuzz.model.JoinConfRequest;
import com.adrosonic.adrobuzz.sync.network.Resource;

import java.util.ArrayList;

/**
 * Created by Lovy on 09-05-2018.
 */

public interface JoinConferenceContract {

    interface View {
        void setLoadingIndicator(boolean active);
        JoinConfRequest getParameters();
    }

    interface Presenter {
        void joinConference();
    }

    interface UseCase {
        void joinConference(JoinConfRequest request, @NonNull JoinConferenceContract.UseCase.Completion completion);

        interface Completion {
            void didReceiveResource(Resource<APIResponse> resource);
        }
    }
}
