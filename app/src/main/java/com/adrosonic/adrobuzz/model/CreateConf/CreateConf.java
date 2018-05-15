package com.adrosonic.adrobuzz.model.CreateConf;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class CreateConf{
  @SerializedName("data")
  private String data;
  @SerializedName("error")
  private String error;
  @SerializedName("status")
  private Integer status;
  public void setData(String data){
   this.data=data;
  }
  public String getData(){
   return data;
  }
  public void setError(String error){
   this.error=error;
  }
  public String getError(){
   return error;
  }
  public void setStatus(Integer status){
   this.status=status;
  }
  public Integer getStatus(){
   return status;
  }
}