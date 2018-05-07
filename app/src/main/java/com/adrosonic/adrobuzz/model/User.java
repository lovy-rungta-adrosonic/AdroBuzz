package com.adrosonic.adrobuzz.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Awesome Pojo Generator
 * */
public class User {
  @SerializedName("userName")
  private String userName;
  @SerializedName("email")
  private String email;
  public void setUserName(String userName){
   this.userName=userName;
  }
  public String getUserName(){
   return userName;
  }
  public void setEmail(String email){
   this.email=email;
  }
  public String getEmail(){
   return email;
  }

    public static class Builder {

        private String mUserName;
        private String mEmail;

        public User.Builder withUserName(String username) {
            mUserName = username;
            return this;
        }

        public User.Builder withEmail(String email) {
            mEmail = email;
            return this;
        }

        public User build() {
            User user = new User();
            user.userName = mUserName;
            user.email = mEmail;
            return user;
        }

    }

}