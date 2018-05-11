package com.adrosonic.adrobuzz.components.Speech2Text;

import android.content.Context;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.contract.SpeechToTextContract;
import com.adrosonic.adrobuzz.interactor.SpeechToTextInteractor;
import com.adrosonic.adrobuzz.model.ConferenceStatus;
import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.adrosonic.adrobuzz.model.DataConfStatus;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.network.Resource;

/**
 * Created by Lovy on 09-05-2018.
 */

public class SpeechToTextPresenter implements SpeechToTextContract.Presenter {

    private static final String TAG = SpeechToTextPresenter.class.getSimpleName();

    private final SpeechToTextContract.View view;
    private final SpeechToTextInteractor mInteractor;
    private Context context;

    public SpeechToTextPresenter(SpeechToTextContract.View view, Context context, Service service) {
        this.view = view;
        this.context = context;
        mInteractor = new SpeechToTextInteractor(context, service);
    }

    @Override
    public String getConfSubject() {
        CreateConfRequest request = PreferenceManager.getInstance(context).getConfParams();
        if (request != null) {
            return request.getName();
        } else {
            return "Subject";
        }
    }

    @Override
    public void endConference() {
        mInteractor.endConference(new SpeechToTextContract.UseCase.Completion() {
            @Override
            public void didReceiveResource(Resource<ConferenceStatus> resource) {
                view.setLoadingIndicator(false);
                switch (resource.status) {
                    case LOADING:
                        view.setLoadingIndicator(true);
                        break;
                    case ERROR:
                        view.showError(resource.message);
                        break;
                    case SUCCESS:
                        view.conferenceEnded();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void getConferenceStatus() {
        mInteractor.getConferenceStatus(new SpeechToTextContract.UseCase.Completion() {
            @Override
            public void didReceiveResource(Resource<ConferenceStatus> resource) {
                view.setLoadingIndicator(false);
                switch (resource.status) {
                    case LOADING:
                        break;
                    case ERROR:
                        break;
                    case SUCCESS:
                        if (resource.data != null) {
                            DataConfStatus dataConfStatus = resource.data.getDataConfStatus();
                            if (dataConfStatus != null) {
                                switch (dataConfStatus.getId()) {
                                    case 1:
                                        view.confStatus(1);
                                        PreferenceManager.getInstance(context).setConferenceStatus(1);
                                        break;
                                    case 2:
                                        view.confStatus(2);
                                        PreferenceManager.getInstance(context).setConferenceStatus(2);
                                        break;
                                    case 3:
                                        view.confStatus(3);
                                        PreferenceManager.getInstance(context).setConferenceStatus(3);
                                        break;
                                    default:
                                        view.confStatus(0);
                                        break;
                                }
                            }
                        } else {
                            view.confStatus(0);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void getConferenceStatusLogOut() {
        mInteractor.getConferenceStatus(new SpeechToTextContract.UseCase.Completion() {
            @Override
            public void didReceiveResource(Resource<ConferenceStatus> resource) {
                view.setLoadingIndicator(false);
                switch (resource.status) {
                    case LOADING:
                        break;
                    case ERROR:
                        break;
                    case SUCCESS:
                        if (resource.data != null) {
                            DataConfStatus dataConfStatus = resource.data.getDataConfStatus();
                            if (dataConfStatus != null) {
                                switch (dataConfStatus.getId()) {
                                    case 1:
                                        PreferenceManager.getInstance(context).setConferenceStatus(1);
                                        view.finalConfStatus(1);
                                        break;
                                    case 2:
                                        PreferenceManager.getInstance(context).setConferenceStatus(2);
                                        view.finalConfStatus(2);
                                        break;
                                    case 3:
                                        PreferenceManager.getInstance(context).setConferenceStatus(3);
                                        view.finalConfStatus(3);
                                        break;
                                    default:
                                        view.finalConfStatus(0);
                                        break;
                                }
                            }
                        } else {
                            view.finalConfStatus(0);
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }
}
