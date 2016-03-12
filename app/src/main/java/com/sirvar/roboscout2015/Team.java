package com.sirvar.roboscout2015;

public class Team {

    private String teamNumber;
    private String region;
    private String school;
    private String teamName;


    private String uID;

    public Team(String teamNumber, String region, String school, String teamName, String uID) {
        this.teamNumber = teamNumber;
        this.region = region;
        this.school = school;
        this.teamName = teamName;
        this.uID = uID;
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

    public String getuID() {
        return uID;
    }
}
