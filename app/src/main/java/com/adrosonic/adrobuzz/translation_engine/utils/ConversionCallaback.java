package com.adrosonic.adrobuzz.translation_engine.utils;
 
/** 
 * Created by Hitesh on 12-07-2016. 
 */ 
public interface ConversionCallaback { 
 
     void onSuccess(String result);
 
     void onCompletion();
 
     void onErrorOccurred(String errorMessage);

} 