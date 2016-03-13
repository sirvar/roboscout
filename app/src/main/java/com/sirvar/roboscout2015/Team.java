package com.sirvar.roboscout2015;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable {

    private String teamNumber;
    private String region;
    private String school;
    private String teamName;

    private String robotType;
    private String mainStratedgy;
    private String sponsors;
    private String programmingDivision;
    private String mechaincalDivision;
    private String electricalDivision;
    private String mediaDivision;
    private String workWellWithUs;
    private String otherInfo;

    private String uID;

    public Team() {

    }

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

    public String getTeamNumber() {
        return teamNumber;
    }

    public String getRegion() {
        return region;
    }

    public String getSchool() {
        return school;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getRobotType() {
        return robotType;
    }

    public String getMainStratedgy() {
        return mainStratedgy;
    }

    public String getSponsors() {
        return sponsors;
    }

    public String getProgrammingDivision() {
        return programmingDivision;
    }

    public String getMechaincalDivision() {
        return mechaincalDivision;
    }

    public String getElectricalDivision() {
        return electricalDivision;
    }

    public String getMediaDivision() {
        return mediaDivision;
    }

    public String getWorkWellWithUs() {
        return workWellWithUs;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public String getuID() {
        return uID;
    }


    public void setTeamNumber(String teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setRobotType(String robotType) {
        this.robotType = robotType;
    }

    public void setMainStratedgy(String mainStratedgy) {
        this.mainStratedgy = mainStratedgy;
    }

    public void setSponsors(String sponsors) {
        this.sponsors = sponsors;
    }

    public void setProgrammingDivision(String programmingDivision) {
        this.programmingDivision = programmingDivision;
    }

    public void setMechaincalDivision(String mechaincalDivision) {
        this.mechaincalDivision = mechaincalDivision;
    }

    public void setElectricalDivision(String electricalDivision) {
        this.electricalDivision = electricalDivision;
    }

    public void setMediaDivision(String mediaDivision) {
        this.mediaDivision = mediaDivision;
    }

    public void setWorkWellWithUs(String workWellWithUs) {
        this.workWellWithUs = workWellWithUs;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
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

}
