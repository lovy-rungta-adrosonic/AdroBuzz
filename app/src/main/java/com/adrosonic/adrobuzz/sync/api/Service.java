package com.adrosonic.adrobuzz.sync.api;

import com.adrosonic.adrobuzz.model.AddInvites.AddInvites;
import com.adrosonic.adrobuzz.model.ConfAttendees.ConfAttendees;
import com.adrosonic.adrobuzz.model.ConfStatus.ConferenceStatus;
import com.adrosonic.adrobuzz.model.CreateConf.CreateConf;
import com.adrosonic.adrobuzz.model.CreateConf.CreateConfRequest;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConf;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConfRequest;
import com.adrosonic.adrobuzz.model.StartConf.StartConf;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Lovy on 19-04-2018.
 */

public interface Service {

    @POST("createConference")
    Call<CreateConf> getConferenceID(@Body CreateConfRequest body);

    @POST("sendInvitations/{param}")
    Call<AddInvites> addInvites(@Path("param") String id, @Body ArrayList<String> body);

    @GET("startConference/{param}")
    Call<StartConf> startConference(@Path("param") String id);

    @GET("getConferenceStatus/{param}")
    Call<ConferenceStatus> getConferenceStatus(@Path("param") String id);

    @GET("endConference/{param}")
    Call<ConferenceStatus> endConference(@Path("param") String id);

    @POST("joinConference/{param}")
    Call<JoinConf> joinConference(@Path("param") String id, @Body JoinConfRequest body);

    @GET("getConferenceAttendes/{param}")
    Call<ConfAttendees> getConferenceAttende(@Path("param") String id);
}
