package com.adrosonic.adrobuzz.sync.api;

import android.content.Context;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.sync.network.Environment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lovy on 19-04-2018.
 */

public class RetrofitClient {

//    public static final String BASE_URL = "http://34.231.184.70/Conference/services/conferenceAPI/";
    private static Retrofit retrofit = null;
    private static String baseURL;

    public static Retrofit getClient() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static String getBaseURL(Context context, Environment environment) {
        baseURL = PreferenceManager.getInstance(context).getBaseURL();
        return baseURL;
    }
}
