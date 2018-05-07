package com.adrosonic.adrobuzz.components.CreateConference;

import android.content.Context;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.contract.StartConferenceContract;
import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.adrosonic.adrobuzz.sync.api.Service;

/**
 * Created by Lovy on 04-05-2018.
 */

public class StartConferencePresenter implements StartConferenceContract.Presenter {

    private static final String TAG = CreateConferencePresenter.class.getSimpleName();

    private final StartConferenceContract.View view;
    private Context context;

    public StartConferencePresenter(StartConferenceContract.View view, Context context, Service service) {
        this.view = view;
        this.context = context;
//        mInteractor = new CreateConferenceInteractor(context,service);
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
}
