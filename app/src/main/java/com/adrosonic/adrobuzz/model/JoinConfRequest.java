package com.adrosonic.adrobuzz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lovy on 09-05-2018.
 */

public class JoinConfRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("conferenceID")
    private String conferenceID;
    @SerializedName("email")
    private String email;

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }

    public String getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(String conferenceID) {
        this.conferenceID = conferenceID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public static class Builder {

        private String mName;
        private String mEmail;
        private String mConfId;

        public JoinConfRequest.Builder withName(String name) {
            mName = name;
            return this;
        }

        public JoinConfRequest.Builder withEmail(String email) {
            mEmail = email;
            return this;
        }

        public JoinConfRequest.Builder withConferenceId(String conferenceId) {
            mConfId = conferenceId;
            return this;
        }

        public JoinConfRequest build() {
            JoinConfRequest request = new JoinConfRequest();
            request.name = mName;
            request.email = mEmail;
            request.conferenceID = mConfId;
            return request;
        }

    }
}
