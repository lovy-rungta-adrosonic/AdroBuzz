package com.adrosonic.adrobuzz.components.CreateConference;

import android.content.Context;
import android.util.Log;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.contract.StartConferenceContract;
import com.adrosonic.adrobuzz.interactor.StartConferenceInteractor;
import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.adrosonic.adrobuzz.model.StartConf;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.network.Resource;

import static com.adrosonic.adrobuzz.sync.network.Status.LOADING;

/**
 * Created by Lovy on 04-05-2018.
 */

public class StartConferencePresenter implements StartConferenceContract.Presenter {

    private static final String TAG = StartConferencePresenter.class.getSimpleName();

    private final StartConferenceContract.View view;
    private final StartConferenceInteractor mInteractor;
    private Context context;

    public StartConferencePresenter(StartConferenceContract.View view, Context context, Service service) {
        this.view = view;
        this.context = context;
        mInteractor = new StartConferenceInteractor(context,service);
    }

    @Override
    public String getConfID() {
        String id = PreferenceManager.getInstance(context).getConfID();
        return "Conference ID: " + id;
    }

    @Override
    public String getVenue() {
        CreateConfRequest request = PreferenceManager.getInstance(context).getConfParams();
        if (request != null) {
            return request.getVenue();
        } else {
            return "";
        }
    }

    @Override
    public String getEmailID() {
        CreateConfRequest request = PreferenceManager.getInstance(context).getConfParams();
        if (request != null) {
            return request.getUser().getEmail();
        } else {
            return "";
        }
    }

    @Override
    public String getUserName() {
        CreateConfRequest request = PreferenceManager.getInstance(context).getConfParams();
        if (request != null) {
            return request.getUser().getUserName();
        } else {
            return "";
        }
    }

    @Override
    public String getDateTime(int index) {
        CreateConfRequest request = PreferenceManager.getInstance(context).getConfParams();
        if (request != null) {
            String dateTime = request.getConferenceDate();
            String[] splitStr = dateTime.split("\\s+");
            switch (index) {
                case 0:
                    return splitStr[0];
                case 1:
                    return splitStr[1];
                default:
                    return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public void startConference() {
        mInteractor.startConference(new StartConferenceContract.UseCase.Completion() {
            @Override
            public void didReceiveResource(Resource< StartConf> resource) {
                view.setLoadingIndicator(false);
                switch (resource.status) {
                    case LOADING:
                        view.setLoadingIndicator(true);
                        break;
                    case ERROR:
                        view.showLoadingError(resource.message);
                        break;
                    case SUCCESS:
//                        Log.v(TAG,"Success");
                        view.conferenceStarted();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
