package com.adrosonic.adrobuzz.sync.api;

import com.adrosonic.adrobuzz.model.ConfAttendees.ConfAttendees;
import com.adrosonic.adrobuzz.model.ConfStatus.ConferenceStatus;
import com.adrosonic.adrobuzz.model.CreateConf.CreateConfRequest;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConf;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConfRequest;
import com.adrosonic.adrobuzz.model.ServiceResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Lovy on 19-04-2018.
 */

public interface Service {

    @POST("createConference")
    Call<ServiceResponse> getConferenceID(@Body CreateConfRequest body);

    @POST("sendInvitations/{param}")
    Call<ServiceResponse> addInvites(@Path("param") String id, @Body ArrayList<String> body);

    @GET("startConference/{param}")
    Call<ServiceResponse> startConference(@Path("param") String id);

    @GET("getConferenceStatus/{param}")
    Call<ConferenceStatus> getConferenceStatus(@Path("param") String id);

    @GET("endConference/{param}")
    Call<ServiceResponse> endConference(@Path("param") String id);

    @POST("joinConference/{param}")
    Call<JoinConf> joinConference(@Path("param") String id, @Body JoinConfRequest body);

    @GET("getConferenceAttendes/{param}")
    Call<ConfAttendees> getConferenceAttende(@Path("param") String id);

    @GET("leaveConference/{param1}/{param2}/{param3}")
    Call<ServiceResponse> leaveConference(@Path("param1") String id, @Path("param2") String email,@Path("param3") String username);

    @Multipart
    @POST("sendJoineeMail/{param1}/{param2}/{param3}")
    Call<ServiceResponse> sendJoineeMail(@Path("param1") String id, @Path("param2") String username,@Path("param3") String email,@Part MultipartBody.Part text);

    @Multipart
    @POST("sendMail/{param1}")
    Call<ServiceResponse> sendMail(@Path("param1") String id,@Part MultipartBody.Part text);
}
