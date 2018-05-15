package com.adrosonic.adrobuzz.model.ConfAttendees;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class ConfAttendees{
  @SerializedName("data")
  @Expose
  private List<AttendeesData> data;
  @SerializedName("error")
  @Expose
  private String error;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setData(List<AttendeesData> data){
   this.data=data;
  }
  public List<AttendeesData> getData(){
   return data;
  }
  public void setError(String  error){
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