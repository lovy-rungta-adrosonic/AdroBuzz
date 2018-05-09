package com.adrosonic.adrobuzz.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lovy on 24-04-2018.
 */

public class PreferenceManager {
    private static final PreferenceManager ourInstance = new PreferenceManager();
    public static final String PREFERENCE_NAME = "com.adrosonic.adrobuzz.Preferences";

    public static final String PREF_BASE_URL = "Base URL";
    public static final String CONFERENCE_ID = "conference_id";
    public static final String CONFERENCE_ID_EXISTS = "conference_id_exists";
    public static final String IS_ADMIN = "is_admin";
    public static final String CONF_PARAMS = "conf_params";
    public static final String LIST_OF_INVITEES = "list_of_invites";
    public static final String IS_CONFERENCE_STARTED = "is_conference_started";
    public static final String CONFERENCE_STATUS = "conference_status";

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

    public void setBaseURL(String baseURL) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putString(PREF_BASE_URL, baseURL)
                    .apply();
        }

    }

    public String getBaseURL() {
        SharedPreferences sharedPreferences = preferences(mContext);

        if (sharedPreferences != null) {
            return sharedPreferences.getString(PREF_BASE_URL, "");

        }
        return "";
    }


    public void setIsAdmin(boolean status) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putBoolean(IS_ADMIN, status)
                    .apply();

        }

    }

    public boolean getIsAdmin() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(IS_ADMIN, false);

        } else {
            return false;
        }

    }

    public void setConfIDExists(boolean exists) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putBoolean(CONFERENCE_ID_EXISTS, exists)
                    .apply();

        }

    }

    public boolean getConfIDExists() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(CONFERENCE_ID_EXISTS, false);

        } else {
            return false;
        }

    }

    public void setConfID(String confID) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putString(CONFERENCE_ID, confID)
                    .apply();

        }
    }

    public String getConfID() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getString(CONFERENCE_ID, "");

        } else {
            return "";
        }

    }

    public void setConfParams(CreateConfRequest request) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String json = gson.toJson(request);
            sharedPreferences.edit()
                    .putString(CONF_PARAMS, json)
                    .apply();
        }

    }

    public CreateConfRequest getConfParams() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString(CONF_PARAMS, "");
            return gson.fromJson(json, CreateConfRequest.class);

        } else {
            return null;
        }

    }

    public void setListOfInvitees(ArrayList<String> listOfInvitees) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            Gson gson = new Gson();

            ArrayList<String> temporary = new ArrayList<>();
            String temp = sharedPreferences.getString(LIST_OF_INVITEES, "");
            String[] items = gson.fromJson(temp, String[].class);
            if (items != null) {
                List<String> listOfEmails = Arrays.asList(items);
                temporary.addAll(listOfEmails);
            }

            temporary.addAll(listOfInvitees);
            String json = gson.toJson(temporary);
            sharedPreferences.edit()
                    .putString(LIST_OF_INVITEES, json)
                    .apply();
        }

    }

    public ArrayList<String> getListOfInvitees() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString(LIST_OF_INVITEES, "");
            String[] items = gson.fromJson(json, String[].class);
            if (items != null) {
                List<String> listOfEmails = Arrays.asList(items);
                return new ArrayList<>(listOfEmails);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void setIsConferenceStarted(boolean started) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putBoolean(IS_CONFERENCE_STARTED, started)
                    .apply();

        }

    }

    public boolean getIsConferenceStarted() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(IS_CONFERENCE_STARTED, false);

        } else {
            return false;
        }


    }

    public void setConferenceStatus(int status) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putInt(CONFERENCE_STATUS, status)
                    .apply();

        }

    }

    public int getConferenceStatus() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(CONFERENCE_STATUS, 0);

        } else {
            return 0;
        }
    }
}
