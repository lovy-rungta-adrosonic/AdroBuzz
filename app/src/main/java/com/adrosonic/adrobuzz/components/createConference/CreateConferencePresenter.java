package com.adrosonic.adrobuzz.components.createConference;

import android.content.Context;

import com.adrosonic.adrobuzz.contract.CreateConferenceContract;
import com.adrosonic.adrobuzz.interactor.CreateConferenceInteractor;
import com.adrosonic.adrobuzz.model.CreateConf.CreateConfRequest;
import com.adrosonic.adrobuzz.model.ServiceResponse;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.network.Resource;

/**
 * Created by Lovy on 20-04-2018.
 */

public class CreateConferencePresenter implements CreateConferenceContract.Presenter {

    private static final String TAG = CreateConferencePresenter.class.getSimpleName();

    private final CreateConferenceContract.View view;
    private final CreateConferenceInteractor mInteractor;

    public CreateConferencePresenter(CreateConferenceContract.View view, Context context, Service service) {
        this.view = view;
        mInteractor = new CreateConferenceInteractor(context,service);
    }

    @Override
    public void fetchConferenceID() {

        final CreateConfRequest request = view.getConferenceParameters();
        mInteractor.fetchConferenceID(request, new CreateConferenceContract.UseCase.Completion() {
            @Override
            public void didReceiveResource(Resource<ServiceResponse> resource) {
                view.setLoadingIndicator(false);
                switch (resource.status) {
                    case LOADING:
                        view.setLoadingIndicator(true);
                        break;
                    case ERROR:
                        view.showLoadingError(resource.message);
                        break;
                    case SUCCESS:
                        view.showConfID();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
