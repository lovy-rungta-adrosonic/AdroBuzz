package com.adrosonic.adrobuzz.components.CreateConference;

import android.content.Context;
import android.util.Log;

import com.adrosonic.adrobuzz.contract.AddInvitesContract;
import com.adrosonic.adrobuzz.interactor.AddInvitesInteractor;
import com.adrosonic.adrobuzz.model.AddInvites;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.network.Resource;

import java.util.ArrayList;

/**
 * Created by Lovy on 04-05-2018.
 */

public class AddInvitesPresenter implements AddInvitesContract.Presenter {
    private static final String TAG = CreateConferencePresenter.class.getSimpleName();

    private final AddInvitesContract.View view;
    private final AddInvitesInteractor mInteractor;

    public AddInvitesPresenter(AddInvitesContract.View view, Context context, Service service) {
        this.view = view;
        mInteractor = new AddInvitesInteractor(context,service);
    }

    @Override
    public void addInvites() {
        final ArrayList<String> request = view.getListOfEmails();

        mInteractor.addInvites(request, new AddInvitesContract.UseCase.Completion() {
            @Override
            public void didReceiveResource(Resource<AddInvites> resource) {
                view.setLoadingIndicator(false);
                switch (resource.status) {
                    case LOADING:
                        view.setLoadingIndicator(true);
                        break;
                    case ERROR:
                        view.showLoadingError(resource.message);
                        break;
                    case SUCCESS:
                        Log.v(TAG,"Success");
//                        view.showConfID(resource.data);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
