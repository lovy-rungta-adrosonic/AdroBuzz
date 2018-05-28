package com.adrosonic.adrobuzz.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.contract.JoinConferenceContract;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConf;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConfRequest;
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

public class JoinConferenceInteractor implements JoinConferenceContract.UseCase {

    private static final String TAG = JoinConferenceInteractor.class.getSimpleName();
    private final Service mService;
    private final AppExecutors mExecutors;
    private Context mContext;

    public JoinConferenceInteractor(Context context,Service service) {
        mService=service;
        mExecutors = new AppExecutors();
        mContext = context;
    }


    @Override
    public void joinConference(final String confId,final JoinConfRequest request, final @NonNull JoinConferenceContract.UseCase.Completion completion) {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                mService.joinConference(confId,request).enqueue(new Callback<JoinConf>() {
                    @Override
                    public void onResponse(Call<JoinConf> call, final Response<JoinConf> response) {
                        if (response.isSuccessful()) {
                            final JoinConf body = response.body();
                            if(body!=null){
                                int status = body.getStatus();
                                if(status == 0){
                                    Log.v(TAG, "joinConference: success: \n"+ body.getStatus());
                                    mExecutors.diskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            PreferenceManager.getInstance(mContext).setIsAdmin(false);
                                            PreferenceManager.getInstance(mContext).setConfID(confId);
                                            PreferenceManager.getInstance(mContext).setConferenceSubject(body.getData().getName());
                                            PreferenceManager.getInstance(mContext).setJoineeEmail(request.getEmail());
                                            PreferenceManager.getInstance(mContext).setJoineeUsername(request.getName());
                                        }
                                    });
                                    mExecutors.mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            completion.didReceiveResource(Resource.success(body));
                                        }
                                    });
                                }else{
                                    mExecutors.mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            JoinConf conf = null;
                                            completion.didReceiveResource(Resource.error(body.getError(),
                                                    conf));
                                        }
                                    });
                                }
                            }else{
                                mExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        JoinConf conf = null;
                                        completion.didReceiveResource(Resource.error("Failed to joinConference",
                                                conf));
                                    }
                                });
                            }
                        } else {
                            try {
                                final String string = response.errorBody()
                                        .string();
                                Log.e(TAG, "joinConference: errorBody: \n" + string);
                            } catch (IOException | NullPointerException e) {
                                Log.e(TAG, "joinConference: errorBody: \n" + e.getMessage());
                            }
                            mExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    JoinConf conf = null;
                                    completion.didReceiveResource(Resource.error("Failed to joinConference",
                                            conf));
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<JoinConf> call, Throwable t) {
                        Log.e(TAG, "joinConference: onFailure: \n", t);
                        JoinConf conf = null;
                        completion.didReceiveResource(Resource.error(t.getMessage(), conf));
                    }
                });
            }
        });
    }
}
