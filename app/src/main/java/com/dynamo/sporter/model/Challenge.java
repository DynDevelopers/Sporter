package com.dynamo.sporter.model;

import com.dynamo.sporter.shared.SharedPrefManager;

public class Challenge {

    private String ID;
    private String clubID;
    private String ageGroup;
    private String date;
    private String teamSize;
    private boolean dateNegotiable;
    private String time;
    private boolean tnegotiable;
    private String location;
    private boolean lNegotiable;
    private String sportname;
    private String description;
    private String status;
    private String createdByID;

    public Challenge() {}
    public Challenge(String clubID, String teamSize, String ageGroup, String date, boolean dateNegotiable, String time, boolean tnegotiable, String location, boolean lNegotiable, String sportname, String description, String status, String createdByID) {

        this.clubID = clubID;
        this.teamSize = teamSize;
        this.ageGroup = ageGroup;
        this.date = date;
        this.time = time;
        this.tnegotiable = tnegotiable;
        this.location = location;
        this.lNegotiable = lNegotiable;
        this.sportname = sportname;
        this.description = description;
        this.status = status;
        this.createdByID = createdByID;
        this.dateNegotiable = dateNegotiable;
    }

    public String getTeamSize() {
        return teamSize;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isTnegotiable() {
        return tnegotiable;
    }

    public String getLocation() {
        return location;
    }

    public boolean islNegotiable() {
        return lNegotiable;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getSportname() {
        return sportname;
    }

    public String getCreatedByID() {
        return createdByID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public boolean isDateNegotiable() {
        return dateNegotiable;
    }

    public String getClubID() {
        return clubID;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "ID='" + ID + '\'' +
                ", clubID='" + clubID + '\'' +
                ", ageGroup='" + ageGroup + '\'' +
                ", date='" + date + '\'' +
                ", teamSize='" + teamSize + '\'' +
                ", dateNegotiable=" + dateNegotiable +
                ", time='" + time + '\'' +
                ", tnegotiable=" + tnegotiable +
                ", location='" + location + '\'' +
                ", lNegotiable=" + lNegotiable +
                ", sportname='" + sportname + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", createdBy='" + createdByID + '\'' +
                '}';
    }
}
