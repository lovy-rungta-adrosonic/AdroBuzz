package com.adrosonic.adrobuzz.model.JoinConf;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class JoinConfUser{
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("userName")
  @Expose
  private String userName;
  @SerializedName("email")
  @Expose
  private String email;
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
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
}