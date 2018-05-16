package com.adrosonic.adrobuzz.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.contract.CreateConferenceContract;
import com.adrosonic.adrobuzz.model.CreateConf.CreateConfRequest;
import com.adrosonic.adrobuzz.model.ServiceResponse;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.network.AppExecutors;
import com.adrosonic.adrobuzz.sync.network.Resource;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lovy on 20-04-2018.
 */

public class CreateConferenceInteractor implements CreateConferenceContract.UseCase {

    private static final String TAG = CreateConferenceInteractor.class.getSimpleName();
    private final Service mService;
    private final AppExecutors mExecutors;
    private Context mContext;

    public CreateConferenceInteractor(Context context,Service service) {
//        RetrofitClient.getBaseURL(context, Environment.DEV);
//        mService = RetrofitClient.getClient().create(Service.class);
        mService=service;
        mExecutors = new AppExecutors();
        mContext = context;
    }


    @Override
    public void fetchConferenceID( final CreateConfRequest request,@NonNull final Completion completion) {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                mService.getConferenceID(request).enqueue(new Callback<ServiceResponse>() {
                    @Override
                    public void onResponse(Call<ServiceResponse> call, final Response<ServiceResponse> response) {
                        if (response.isSuccessful()) {
                            final ServiceResponse body = response.body();
                            Log.v(TAG, "fetchConferenceID: success: \n"+ body.getData());
                            mExecutors.diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    PreferenceManager.getInstance(mContext).setIsAdmin(true);
                                    PreferenceManager.getInstance(mContext).setConfID(body.getData());
                                    PreferenceManager.getInstance(mContext).setConfParams(request);
                                    PreferenceManager.getInstance(mContext).setConferenceSubject(request.getName());
                                }
                            });
                            mExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    completion.didReceiveResource(Resource.success(body));
                                }
                            });
                        } else {
                            try {
                                final String string = response.errorBody()
                                        .string();
                                Log.e(TAG, "fetchConferenceID: errorBody: \n" + string);
                            } catch (IOException | NullPointerException e) {
                                Log.e(TAG, "fetchConferenceID: errorBody: \n" + e.getMessage());
                            }
                            mExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    ServiceResponse conf = null;
                                    completion.didReceiveResource(Resource.error("Failed to fetchConferenceID",
                                            conf));
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceResponse> call, Throwable t) {
                        Log.e(TAG, "fetchConferenceID: onFailure: \n", t);
                        ServiceResponse conf = null;
                        completion.didReceiveResource(Resource.error(t.getMessage(), conf));
                    }
                });
            }
        });

    }
}
