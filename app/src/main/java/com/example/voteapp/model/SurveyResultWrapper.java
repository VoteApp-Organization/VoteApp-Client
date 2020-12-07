package com.example.voteapp.model;

import java.util.List;

public class SurveyResultWrapper {
    public List<QuestionAnswersWrapper> questions;
    public String surveyTitle;
    public String surveyDescription;
    public Double voteTurnout;
    public String createdDate;
    public Long author_id;
    public String startDate;
    public String endDate;
    public Boolean isPublicVote;
    public Boolean isAnonymousVote;
    public Boolean isMandatory;
    public Integer numberOfQuestions;
    public Integer enabledToVote;
    public Integer peopleWhoHasVote;

    public SurveyResultWrapper() {
    }

    public SurveyResultWrapper(Survey vote) {
        this.surveyTitle = vote.getVoteTitle();
        this.surveyDescription = vote.getSurveyDescription();
        this.createdDate = vote.getCreatedDate();
        this.author_id = vote.getAuthor_id();
        this.startDate = vote.getStartDate();
        this.endDate = vote.getEndDate();
        this.isPublicVote = vote.getPublicVote();
        this.isAnonymousVote = vote.getAnonymousVote();
        this.isMandatory = vote.getMandatory();
        this.numberOfQuestions = vote.getNumberOfQuestions();
    }
}
