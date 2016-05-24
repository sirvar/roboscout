package com.sirvar.roboscout;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable {

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
    private String teamNumber = "";
    private String region = "";
    private String school = "";
    private String teamName = "";
    private String uID = "";

    // Empty constructor for blank team
    public Team() {

    }

    // Constructor to set data
    public Team(String teamNumber, String region, String school, String teamName, String uID) {
        this.teamNumber = teamNumber;
        this.region = region;
        this.school = school;
        this.teamName = teamName;
        this.uID = uID;
    }

    // Parcelable constructor
    public Team(Parcel in) {
        String[] parcel = new String[5];

        in.readStringArray(parcel);

        this.teamNumber = parcel[0];
        this.region = parcel[1];
        this.school = parcel[2];
        this.teamName = parcel[3];
        this.uID = parcel[4];
    }

    // Getter and setter methods

    public String getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(String teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    // Implementing methods for Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] parcel = {teamNumber, region, school, teamName, uID};
        dest.writeStringArray(parcel);
    }

}
