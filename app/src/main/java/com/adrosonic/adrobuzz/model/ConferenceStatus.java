package com.adrosonic.adrobuzz.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class ConferenceStatus{
  @SerializedName("data")
  @Expose
  private Data data;
  @SerializedName("error")
  @Expose
  private Object error;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setData(Data data){
   this.data=data;
  }
  public Data getData(){
   return data;
  }
  public void setError(Object error){
   this.error=error;
  }
  public Object getError(){
   return error;
  }
  public void setStatus(Integer status){
   this.status=status;
  }
  public Integer getStatus(){
   return status;
  }
}