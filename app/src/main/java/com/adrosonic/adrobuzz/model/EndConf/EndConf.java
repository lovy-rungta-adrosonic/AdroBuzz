package com.adrosonic.adrobuzz.model.EndConf;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lovy on 15-05-2018.
 */

public class EndConf {
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
