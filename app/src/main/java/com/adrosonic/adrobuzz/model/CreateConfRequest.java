package com.adrosonic.adrobuzz.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Awesome Pojo Generator
 * */
public class CreateConfRequest {
  @SerializedName("venue")
  private String venue;
  @SerializedName("conferenceDateTime")
  private String conferenceDate;
  @SerializedName("name")
  private String name;
  @SerializedName("user")
  private User user;
  public void setVenue(String venue){
   this.venue=venue;
  }
  public String getVenue(){
   return venue;
  }
  public void setConferenceDate(String conferenceDate){
   this.conferenceDate=conferenceDate;
  }
  public String getConferenceDate(){
   return conferenceDate;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setUser(User user){
   this.user=user;
  }
  public User getUser(){
   return user;
  }

    public static class Builder {

        private String mVenue;
        private String mConferenceDate;
        private String mName;
        private User mUser;

        public CreateConfRequest.Builder withName(String name) {
           mName = name;
            return this;
        }

        public CreateConfRequest.Builder withVenue(String venue) {
           mVenue = venue;
            return this;
        }

        public CreateConfRequest.Builder withConferenceDate(String date) {
           mConferenceDate = date;
            return this;
        }

        public CreateConfRequest.Builder withUser(User user) {
           mUser = user;
            return this;
        }

        public CreateConfRequest build() {
            CreateConfRequest request = new CreateConfRequest();
            request.name = mName;
            request.venue = mVenue;
            request.conferenceDate = mConferenceDate;
            request.user = mUser;
            return request;
        }

    }
}