package com.adrosonic.adrobuzz.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.google.gson.Gson;

/**
 * Created by Lovy on 24-04-2018.
 */

public class PreferenceManager {
    private static final PreferenceManager ourInstance = new PreferenceManager();
    public static final String PREFERENCE_NAME = "com.adrosonic.adrobuzz.Preferences";

    public static final String PREF_BASE_URL="Base URL";
    public static final String CONFERENCE_ID = "conference_id";
    public static final String CONFERENCE_ID_EXISTS = "conference_id_exists";
    public static final String IS_ADMIN = "is_admin";
    public static final String CONF_PARAMS = "conf_params";


    private Context mContext;

    public static PreferenceManager getInstance(Context context) {
        ourInstance.mContext = context;
        return ourInstance;
    }

    private SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context
                .MODE_PRIVATE);
    }

    public PreferenceManager() {
    }

    public void setBaseURL(String baseURL){
        SharedPreferences sharedPreferences=preferences(mContext);
        if(sharedPreferences!=null){
            sharedPreferences.edit()
                    .putString(PREF_BASE_URL, baseURL)
                    .apply();
        }

    }

    public String getBaseURL(){
        SharedPreferences sharedPreferences=preferences(mContext);

        if (sharedPreferences!=null) {
            return sharedPreferences.getString(PREF_BASE_URL,"");

        }
        return "";
    }


    public void setIsAdmin(boolean status){
        SharedPreferences sharedPreferences=preferences(mContext);
        if(sharedPreferences!=null){
            sharedPreferences.edit()
                    .putBoolean(IS_ADMIN, status)
                    .apply();

        }

    }

    public boolean getIsAdmin(){
        SharedPreferences sharedPreferences=preferences(mContext);
        if(sharedPreferences!=null){
            return sharedPreferences.getBoolean(IS_ADMIN,false);

        }
        else{
            return false;
        }

    }

    public void setConfIDExists(boolean exists){
        SharedPreferences sharedPreferences=preferences(mContext);
        if(sharedPreferences!=null){
            sharedPreferences.edit()
                    .putBoolean(CONFERENCE_ID_EXISTS, exists)
                    .apply();

        }

    }

    public boolean getConfIDExists(){
        SharedPreferences sharedPreferences=preferences(mContext);
        if(sharedPreferences!=null){
            return sharedPreferences.getBoolean(CONFERENCE_ID_EXISTS,false);

        }
        else{
            return false;
        }

    }

    public void setConfID(String confID){
        SharedPreferences sharedPreferences=preferences(mContext);
        if(sharedPreferences!=null){
            sharedPreferences.edit()
                    .putString(CONFERENCE_ID, confID)
                    .apply();

        }

    }

    public String getConfID(){
        SharedPreferences sharedPreferences=preferences(mContext);
        if(sharedPreferences!=null){
            return sharedPreferences.getString(CONFERENCE_ID,"");

        }
        else{
            return "";
        }

    }

    public void setConfParams(CreateConfRequest request){
        SharedPreferences sharedPreferences=preferences(mContext);
        if(sharedPreferences!=null){
            Gson gson = new Gson();
            String json = gson.toJson(request);
            sharedPreferences.edit()
                    .putString(CONF_PARAMS, json)
                    .apply();

        }

    }

    public CreateConfRequest getConfParams(){
        SharedPreferences sharedPreferences=preferences(mContext);
        if(sharedPreferences!=null){
            Gson gson = new Gson();
            String json = sharedPreferences.getString(CONF_PARAMS,"");
            return gson.fromJson(json,CreateConfRequest.class);

        }
        else{
            return null;
        }

    }
}
