package com.example.voteapp;

import java.util.Date;

public class Survey {

    private Long vote_Id;
    private String voteTitle;
    private Date createdDate;
    private Long author_id;
    private Date startDate;
    private Date endDate;
    private Boolean isPublicVote;
    private Boolean isAnonymousVote;
    private Boolean isMandatory;
    public Boolean answerHasBeenGiven;
    public String voteDate;
    public Integer numberOfQuestions;
    public String surveyPicture;

    public Survey(Long vote_Id, String voteTitle, Date createdDate, Long author_id, Date startDate, Date endDate, Boolean isPublicVote, Boolean isAnonymousVote, Boolean isMandatory, Boolean answerHasBeenGiven, String voteDate, Integer numberOfQuestions, String surveyPicture) {
        this.vote_Id = vote_Id;
        this.voteTitle = voteTitle;
        this.createdDate = createdDate;
        this.author_id = author_id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublicVote = isPublicVote;
        this.isAnonymousVote = isAnonymousVote;
        this.isMandatory = isMandatory;
        this.answerHasBeenGiven = answerHasBeenGiven;
        this.voteDate = voteDate;
        this.numberOfQuestions = numberOfQuestions;
        this.surveyPicture = surveyPicture;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Long author_id) {
        this.author_id = author_id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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
}
