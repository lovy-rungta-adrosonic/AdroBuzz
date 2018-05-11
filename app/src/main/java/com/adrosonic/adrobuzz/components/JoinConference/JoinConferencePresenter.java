package com.adrosonic.adrobuzz.components.JoinConference;

import android.content.Context;
import android.util.Log;

import com.adrosonic.adrobuzz.components.CreateConference.CreateConferencePresenter;
import com.adrosonic.adrobuzz.contract.JoinConferenceContract;
import com.adrosonic.adrobuzz.interactor.JoinConferenceInteractor;
import com.adrosonic.adrobuzz.model.APIResponse;
import com.adrosonic.adrobuzz.model.JoinConfRequest;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.network.Resource;

/**
 * Created by Lovy on 09-05-2018.
 */

public class JoinConferencePresenter implements JoinConferenceContract.Presenter {

    private static final String TAG = JoinConferencePresenter.class.getSimpleName();
    private final JoinConferenceContract.View view;
    private final JoinConferenceInteractor mInteractor;

    public JoinConferencePresenter(JoinConferenceContract.View view, Context context, Service service) {
        this.view = view;
        mInteractor = new JoinConferenceInteractor(context,service);
    }

    @Override
    public void joinConference() {

        final JoinConfRequest request = view.getParameters();
        final String confId = view.getConfId();
        mInteractor.joinConference( confId, request, new JoinConferenceContract.UseCase.Completion() {
            @Override
            public void didReceiveResource(Resource<APIResponse> resource) {
                view.setLoadingIndicator(false);
                switch (resource.status) {
                    case LOADING:
                        view.setLoadingIndicator(true);
                        break;
                    case ERROR:
//                        view.showLoadingError(resource.message);
                        break;
                    case SUCCESS:
                        Log.v(TAG,"Successfully joined conference");
//                        view.showConfID(resource.data);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
