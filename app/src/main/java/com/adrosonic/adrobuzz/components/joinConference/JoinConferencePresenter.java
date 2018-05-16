package com.adrosonic.adrobuzz.components.joinConference;

import android.content.Context;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.contract.JoinConferenceContract;
import com.adrosonic.adrobuzz.interactor.JoinConferenceInteractor;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConf;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConfRequest;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.network.Resource;

/**
 * Created by Lovy on 09-05-2018.
 */

public class JoinConferencePresenter implements JoinConferenceContract.Presenter {

    private static final String TAG = JoinConferencePresenter.class.getSimpleName();
    private final JoinConferenceContract.View view;
    private final JoinConferenceInteractor mInteractor;
    private final Context context;

    public JoinConferencePresenter(JoinConferenceContract.View view, Context context, Service service) {
        this.view = view;
        mInteractor = new JoinConferenceInteractor(context,service);
        this.context = context;
    }

    @Override
    public void joinConference() {

        final JoinConfRequest request = view.getParameters();
        final String confId = view.getConfId();
        mInteractor.joinConference( confId, request, new JoinConferenceContract.UseCase.Completion() {
            @Override
            public void didReceiveResource(Resource<JoinConf> resource) {
                view.setLoadingIndicator(false);
                switch (resource.status) {
                    case LOADING:
                        view.setLoadingIndicator(true);
                        break;
                    case ERROR:
                        view.showLoadingError(resource.message);
                        break;
                    case SUCCESS:
                        JoinConf data = resource.data;
                        if(data!=null){
                            int status =data.getData().getStatus().getId();
                            switch(status){
                                case 1:
                                    view.showLoadingError(context.getString(R.string.join_error_conference_not_started));
                                    break;
                                case 2:
                                    view.joinedConference(resource.data);
                                    break;
                                case 3:
                                    view.showLoadingError(context.getString(R.string.join_error_conference_completed));
                                    break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
