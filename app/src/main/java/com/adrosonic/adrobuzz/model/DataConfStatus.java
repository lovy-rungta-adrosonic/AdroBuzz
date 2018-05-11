package com.adrosonic.adrobuzz.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class DataConfStatus {
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("id")
  @Expose
  private Integer id;
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
}