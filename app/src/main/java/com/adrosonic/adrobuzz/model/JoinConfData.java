package com.adrosonic.adrobuzz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class JoinConfData {
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("startTimeStamp")
    @Expose
    private long startTimeStamp;
    @SerializedName("conferenceId")
    @Expose
    private String conferenceId;
    @SerializedName("conferenceDate")
    @Expose
    private long conferenceDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("createdTimeStamp")
    @Expose
    private long createdTimeStamp;
    @SerializedName("endTimeStamp")
    @Expose
    private long endTimeStamp;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("conferenceDateTime")
    @Expose
    private long conferenceDateTime;
    @SerializedName("user")
    @Expose
    private JoinConfUser user;
    @SerializedName("status")
    @Expose
    private JoinConfStatus status;

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getVenue() {
        return venue;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceDate(long conferenceDate) {
        this.conferenceDate = conferenceDate;
    }

    public long getConferenceDate() {
        return conferenceDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCreatedTimeStamp(Integer createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public long getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public Object getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setConferenceDateTime(long conferenceDateTime) {
        this.conferenceDateTime = conferenceDateTime;
    }

    public Object getConferenceDateTime() {
        return conferenceDateTime;
    }

    public void setUser(JoinConfUser user) {
        this.user = user;
    }

    public JoinConfUser getUser() {
        return user;
    }

    public void setStatus(JoinConfStatus status) {
        this.status = status;
    }

    public JoinConfStatus getStatus() {
        return status;
    }
}