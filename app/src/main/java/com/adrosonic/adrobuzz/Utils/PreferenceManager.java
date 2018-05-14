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
    private static final String PREFERENCE_NAME = "com.adrosonic.adrobuzz.Preferences";

    private static final String CONFERENCE_ID = "conference_id";
    private static final String ADMIN_ENDED_CONFERENCE = "admin_ended_conference";
    private static final String IS_ADMIN = "is_admin";
    private static final String CONF_PARAMS = "conf_params";
    private static final String LIST_OF_INVITEES = "list_of_invites";
    private static final String IS_CONFERENCE_STARTED = "is_conference_started";
    private static final String CONFERENCE_STATUS = "conference_status";
    private static final String EMAIL_SENT = "email_sent";
    private static final String CONFERENCE_SUMMARY = "conference_summary";
    private static final String IS_JOINED_CONFERENCE = "is_joined_conference";
    private static final String CONFERENCE_SUBJECT = "conference_subject";

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

    public void clearSharedPreferences(){
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .clear()
                    .apply();

        }
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

    public void setAdminEndedConference(boolean status) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putBoolean(ADMIN_ENDED_CONFERENCE, status)
                    .apply();

        }

    }

    public boolean getAdminEndedConference() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(ADMIN_ENDED_CONFERENCE, false);

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

    public void setEmailSent(boolean status) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putBoolean(EMAIL_SENT, status)
                    .apply();

        }

    }

    public boolean getEmailSent() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(EMAIL_SENT, false);

        } else {
            return false;
        }
    }

    public void setConferenceSummary(String summary) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putString(CONFERENCE_SUMMARY, summary)
                    .apply();

        }
    }

    public String getConferenceSummary() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getString(CONFERENCE_SUMMARY, "");

        } else {
            return "";
        }

    }

    public void setIsJoinedConference(boolean status) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putBoolean(IS_JOINED_CONFERENCE, status)
                    .apply();

        }

    }

    public boolean getIsJoinedConference() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(IS_JOINED_CONFERENCE, false);

        } else {
            return false;
        }

    }

    public void setConferenceSubject(String subject) {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putString(CONFERENCE_SUBJECT, subject)
                    .apply();

        }
    }

    public String getConferenceSubject() {
        SharedPreferences sharedPreferences = preferences(mContext);
        if (sharedPreferences != null) {
            return sharedPreferences.getString(CONFERENCE_SUBJECT, "");

        } else {
            return "";
        }

    }

}
