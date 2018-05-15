package com.adrosonic.adrobuzz.model.CreateConf;
import com.google.gson.annotations.SerializedName;

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
  private CreateConfUser createConfUser;
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
  public void setCreateConfUser(CreateConfUser createConfUser){
   this.createConfUser = createConfUser;
  }
  public CreateConfUser getCreateConfUser(){
   return createConfUser;
  }

    public static class Builder {

        private String mVenue;
        private String mConferenceDate;
        private String mName;
        private CreateConfUser mCreateConfUser;

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

        public CreateConfRequest.Builder withUser(CreateConfUser createConfUser) {
           mCreateConfUser = createConfUser;
            return this;
        }

        public CreateConfRequest build() {
            CreateConfRequest request = new CreateConfRequest();
            request.name = mName;
            request.venue = mVenue;
            request.conferenceDate = mConferenceDate;
            request.createConfUser = mCreateConfUser;
            return request;
        }

    }
}