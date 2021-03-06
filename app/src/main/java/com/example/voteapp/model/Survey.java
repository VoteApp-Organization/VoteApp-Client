package com.example.voteapp.model;

import java.io.Serializable;
import java.util.Date;

public class Survey implements Serializable {

    private Long vote_Id;
    private String voteTitle;
    public String surveyDescription;
    private String createdDate;
    private Long author_id;
    private String startDate;
    private String endDate;
    private Boolean isPublicVote;
    private Boolean isAnonymousVote;
    private Boolean isMandatory;
    public Boolean answerHasBeenGiven;
    public String voteDate;
    public Integer numberOfQuestions;
    public String surveyPicture;
    public Boolean authorIsVoting;

    public Survey(String voteTitle, String surveyDescription, Long author_id, String startDate, Boolean isPublicVote, Boolean isAnonymousVote, Boolean isMandatory, Integer numberOfQuestions, String surveyPicture, Boolean authorIsVoting ) {
        this.voteTitle = voteTitle;
        this.surveyDescription = surveyDescription;
        this.author_id = author_id;
        this.startDate = startDate;
        this.isPublicVote = isPublicVote;
        this.isAnonymousVote = isAnonymousVote;
        this.isMandatory = isMandatory;
        this.numberOfQuestions = numberOfQuestions;
        this.surveyPicture = surveyPicture;
        this.authorIsVoting  = authorIsVoting;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }

    public Long getVote_Id() {
        return vote_Id;
    }

    public void setVote_Id(Long vote_Id) {
        this.vote_Id = vote_Id;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Long author_id) {
        this.author_id = author_id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getPublicVote() {
        return isPublicVote;
    }

    public void setPublicVote(Boolean publicVote) {
        isPublicVote = publicVote;
    }

    public Boolean getAnonymousVote() {
        return isAnonymousVote;
    }

    public void setAnonymousVote(Boolean anonymousVote) {
        isAnonymousVote = anonymousVote;
    }

    public Boolean getMandatory() {
        return isMandatory;
    }

    public void setMandatory(Boolean mandatory) {
        isMandatory = mandatory;
    }

    public Boolean getAnswerHasBeenGiven() {
        return answerHasBeenGiven;
    }

    public void setAnswerHasBeenGiven(Boolean answerHasBeenGiven) {
        this.answerHasBeenGiven = answerHasBeenGiven;
    }

    public String getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(String voteDate) {
        this.voteDate = voteDate;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public String getSurveyPicture() {
        return surveyPicture;
    }

    public void setSurveyPicture(String surveyPicture) {
        this.surveyPicture = surveyPicture;
    }

    public Boolean getAuthorIsVoting() {
        return authorIsVoting;
    }
    public void setAuthorIsVoting(Boolean authorIsVoting) {
        this.authorIsVoting = authorIsVoting;
    }
}
