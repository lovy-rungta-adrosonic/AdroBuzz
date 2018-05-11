package com.adrosonic.adrobuzz.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.contract.SpeechToTextContract;
import com.adrosonic.adrobuzz.model.ConferenceStatus;
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

public class SpeechToTextInteractor implements SpeechToTextContract.UseCase {

    private static final String TAG = SpeechToTextInteractor.class.getSimpleName();
    private final Service mService;
    private final AppExecutors mExecutors;
    private Context mContext;

    public SpeechToTextInteractor(Context context, Service service) {
        mService = service;
        mExecutors = new AppExecutors();
        mContext = context;
    }

    @Override
    public void endConference(final @NonNull Completion completion) {

        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                String confId = PreferenceManager.getInstance(mContext).getConfID();

                mService.endConference(confId).enqueue(new Callback<ConferenceStatus>() {
                    @Override
                    public void onResponse(Call<ConferenceStatus> call, final Response<ConferenceStatus> response) {
                        if (response.isSuccessful()) {
                            final ConferenceStatus body = response.body();
                            if (body != null && body.getStatus() == 0) {
                                Log.v(TAG, "endConference: success: \n" + body.getStatus());
                                mExecutors.diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        PreferenceManager.getInstance(mContext).setConferenceStatus(3);
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
                                        ConferenceStatus conf = null;
                                        completion.didReceiveResource(Resource.error("Failed to endConference",
                                                conf));
                                    }
                                });
                            }

                        } else {
                            try {
                                final String string = response.errorBody()
                                        .string();
                                Log.e(TAG, "endConference: errorBody: \n" + string);
                            } catch (IOException | NullPointerException e) {
                                Log.e(TAG, "endConference: errorBody: \n" + e.getMessage());
                            }

                            mExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    ConferenceStatus conf = null;
                                    completion.didReceiveResource(Resource.error("Failed to end conference",
                                            conf));
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ConferenceStatus> call, Throwable t) {
                        Log.e(TAG, "endConference: onFailure: \n", t);
                        ConferenceStatus conf = null;
                        completion.didReceiveResource(Resource.error(t.getMessage(), conf));
                    }
                });
            }
        });
    }

    @Override
    public void getConferenceStatus(final @NonNull Completion completion) {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {

                String confId = PreferenceManager.getInstance(mContext).getConfID();

                mService.getConferenceStatus(confId).enqueue(new Callback<ConferenceStatus>() {
                    @Override
                    public void onResponse(Call<ConferenceStatus> call, final Response<ConferenceStatus> response) {
                        if (response.isSuccessful()) {
                            final ConferenceStatus body = response.body();
                            if (body != null && body.getStatus() == 0) {
                                Log.v(TAG, "getConferenceStatus: success: \n" + body.getStatus());
                                Log.v(TAG, "getConferenceStatus: success: \n" + body.getDataConfStatus().getName());

                                mExecutors.diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
//                                        PreferenceManager.getInstance(mContext).setIsConferenceStarted(true);
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
                                        ConferenceStatus conf = null;
                                        completion.didReceiveResource(Resource.error("Failed to getConferenceStatus",
                                                conf));
                                    }
                                });
                            }

                        } else {
                            try {
                                final String string = response.errorBody()
                                        .string();
                                Log.e(TAG, "getConferenceStatus: errorBody: \n" + string);
                            } catch (IOException | NullPointerException e) {
                                Log.e(TAG, "getConferenceStatus: errorBody: \n" + e.getMessage());
                            }

                            mExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    ConferenceStatus conf = null;
                                    completion.didReceiveResource(Resource.error("Failed to getConferenceStatus",
                                            conf));
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ConferenceStatus> call, Throwable t) {
                        Log.e(TAG, "getConferenceStatus: onFailure: \n", t);
                        ConferenceStatus conf = null;
                        completion.didReceiveResource(Resource.error(t.getMessage(), conf));
                    }
                });
            }
        });

    }
}
