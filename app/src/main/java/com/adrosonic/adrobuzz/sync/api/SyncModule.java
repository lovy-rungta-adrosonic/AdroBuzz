package com.adrosonic.adrobuzz.sync.api;

import com.adrosonic.adrobuzz.sync.network.Configuration;
import com.adrosonic.adrobuzz.sync.network.Environment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lovy on 26-04-2018.
 */

@Module
public class SyncModule {

    private Environment mEnvironment;

    public SyncModule(Environment environment) {
        this.mEnvironment = environment;
    }

    @Provides
    public Environment provideEnvironment() {
        return mEnvironment;
    }

    @Provides
    @Singleton
    public Configuration provideConfiguration(Environment environment) {
        return new Configuration(environment);
    }

    @Provides
    @Singleton
    public Retrofit provideClient(Configuration configuration) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(configuration.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

    @Provides
    @Singleton
    public Service provideService(Retrofit retrofit) {
        return retrofit.create(Service.class);
    }

}

