package com.adrosonic.adrobuzz.sync.api;

import com.adrosonic.adrobuzz.model.AddInvites;
import com.adrosonic.adrobuzz.model.CreateConf;
import com.adrosonic.adrobuzz.model.CreateConfRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
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
}
