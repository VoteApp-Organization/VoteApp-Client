package com.example.voteapp.model;

public class GroupsToExploreWrapper {
    public Group group;
    public Long numberOfUsers;
    public Long numberOfSurveys;

    public GroupsToExploreWrapper(Group group, Long numberOfUsers, Long numberOfSurveys) {
        this.group = group;
        this.numberOfUsers = numberOfUsers;
        this.numberOfSurveys = numberOfSurveys;
    }
}
