package com.adrosonic.adrobuzz.model.CreateConf;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 * */
public class CreateConfUser {
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

        public CreateConfUser.Builder withUserName(String username) {
            mUserName = username;
            return this;
        }

        public CreateConfUser.Builder withEmail(String email) {
            mEmail = email;
            return this;
        }

        public CreateConfUser build() {
            CreateConfUser createConfUser = new CreateConfUser();
            createConfUser.userName = mUserName;
            createConfUser.email = mEmail;
            return createConfUser;
        }

    }

}