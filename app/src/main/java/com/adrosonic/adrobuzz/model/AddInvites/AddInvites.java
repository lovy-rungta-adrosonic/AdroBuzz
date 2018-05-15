package com.adrosonic.adrobuzz.model.AddInvites;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class AddInvites{
  @SerializedName("data")
  @Expose
  private Object data;
  @SerializedName("error")
  @Expose
  private String error;
  @SerializedName("status")
  @Expose
  private Integer status;
  public void setData(Object data){
   this.data=data;
  }
  public Object getData(){
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