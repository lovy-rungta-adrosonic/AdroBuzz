package com.adrosonic.adrobuzz.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.contract.AddInvitesContract;
import com.adrosonic.adrobuzz.model.AddInvites.AddInvites;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.sync.network.AppExecutors;
import com.adrosonic.adrobuzz.sync.network.Resource;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lovy on 04-05-2018.
 */

public class AddInvitesInteractor implements AddInvitesContract.UseCase {

    private static final String TAG = AddInvitesInteractor.class.getSimpleName();
    private final Service mService;
    private final AppExecutors mExecutors;
    private Context mContext;

    public AddInvitesInteractor(Context context, Service service) {
        mService = service;
        mExecutors = new AppExecutors();
        mContext = context;
    }

    @Override
    public void addInvites(final ArrayList<String> request, final @NonNull Completion completion) {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                String confId = PreferenceManager.getInstance(mContext).getConfID();

                mService.addInvites(confId, request).enqueue(new Callback<AddInvites>() {
                    @Override
                    public void onResponse(Call<AddInvites> call, final Response<AddInvites> response) {
                        if (response.isSuccessful()) {
                            final AddInvites body = response.body();
                            if (body != null && body.getStatus() == 0) {
                                Log.v(TAG, "addInvites: success: \n" + body.getStatus());
                                mExecutors.diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        PreferenceManager.getInstance(mContext).setListOfInvitees(request);
                                    }
                                });
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
                                        AddInvites conf = null;
                                        completion.didReceiveResource(Resource.error("Failed to addInvites",
                                                conf));
                                    }
                                });
                            }

                        } else {
                            try {
                                final String string = response.errorBody()
                                        .string();
                                Log.e(TAG, "addInvites: errorBody: \n" + string);
                            } catch (IOException | NullPointerException e) {
                                Log.e(TAG, "addInvites: errorBody: \n" + e.getMessage());
                            }
                            mExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    AddInvites conf = null;
                                    completion.didReceiveResource(Resource.error("Failed to addInvites",
                                            conf));
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<AddInvites> call, Throwable t) {
                        Log.e(TAG, "addInvites: onFailure: \n", t);
                        AddInvites conf = null;
                        completion.didReceiveResource(Resource.error(t.getMessage(), conf));
                    }
                });
            }
        });
    }
}
