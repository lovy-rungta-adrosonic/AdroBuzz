package com.adrosonic.adrobuzz.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class ConferenceStatus{
  @SerializedName("data")
  @Expose
  private DataConfStatus dataConfStatus;
  @SerializedName("error")
  @Expose
  private Object error;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setDataConfStatus(DataConfStatus dataConfStatus){
   this.dataConfStatus = dataConfStatus;
  }
  public DataConfStatus getDataConfStatus(){
   return dataConfStatus;
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