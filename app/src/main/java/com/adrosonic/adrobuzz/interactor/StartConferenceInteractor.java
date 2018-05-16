package com.adrosonic.adrobuzz.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.contract.StartConferenceContract;
import com.adrosonic.adrobuzz.model.ServiceResponse;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.network.AppExecutors;
import com.adrosonic.adrobuzz.sync.network.Resource;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lovy on 09-05-2018.
 */

public class StartConferenceInteractor implements StartConferenceContract.UseCase {

    private static final String TAG = StartConferenceInteractor.class.getSimpleName();
    private final Service mService;
    private final AppExecutors mExecutors;
    private Context mContext;

    public StartConferenceInteractor(Context context, Service service) {
        mService = service;
        mExecutors = new AppExecutors();
        mContext = context;
    }

    @Override
    public void startConference(final @NonNull Completion completion) {

        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                String confId = PreferenceManager.getInstance(mContext).getConfID();

                mService.startConference(confId).enqueue(new Callback<ServiceResponse>() {
                    @Override
                    public void onResponse(Call<ServiceResponse> call, final Response<ServiceResponse> response) {
                        if (response.isSuccessful()) {
                            final ServiceResponse body = response.body();
                            if (body != null && body.getStatus() == 0) {
                                Log.v(TAG, "StartConference: success: \n" + body.getStatus());
                                mExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        completion.didReceiveResource(Resource.success(body));
                                    }
                                });
                            } else {

                                mExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        ServiceResponse conf = null;
                                        completion.didReceiveResource(Resource.error("Failed to StartConference",
                                                conf));
                                    }
                                });
                            }

                        } else {
                            try {
                                final String string = response.errorBody()
                                        .string();
                                Log.e(TAG, "StartConference: errorBody: \n" + string);
                            } catch (IOException | NullPointerException e) {
                                Log.e(TAG, "StartConference: errorBody: \n" + e.getMessage());
                            }

                            mExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    ServiceResponse conf = null;
                                    completion.didReceiveResource(Resource.error("Failed to StartConference",
                                            conf));
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceResponse> call, Throwable t) {
                        Log.e(TAG, "StartConference: onFailure: \n", t);
                        ServiceResponse conf = null;
                        completion.didReceiveResource(Resource.error(t.getMessage(), conf));
                    }
                });
            }
        });

    }
}
