package com.example.voteapp;

import java.io.Serializable;
import java.util.List;

public class SingleQuestion implements Serializable {

    public Long id;
    public Long vote_id;
    public String questionContent;
    public Boolean multipleChoice;
    public Boolean mandatoryQuestion;
    public Integer maximumCapacityOfAnswer;
    public String questionType;
    public List<String> picklistValues;

    public SingleQuestion(Long id, Long vote_id, String questionContent, Boolean multipleChoice, Boolean mandatoryQuestion, Integer maximumCapacityOfAnswer, String questionType, List<String> picklistValues) {
        this.id = id;
        this.vote_id = vote_id;
        this.questionContent = questionContent;
        this.multipleChoice = multipleChoice;
        this.mandatoryQuestion = mandatoryQuestion;
        this.maximumCapacityOfAnswer = maximumCapacityOfAnswer;
        this.questionType = questionType;
        this.picklistValues = picklistValues;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<String> getPicklistValues() {
        return picklistValues;
    }

    public void setPicklistValues(List<String> picklistValues) {
        this.picklistValues = picklistValues;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVote_id() {
        return vote_id;
    }

    public void setVote_id(Long vote_id) {
        this.vote_id = vote_id;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public Boolean getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(Boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public Boolean getMandatoryQuestion() {
        return mandatoryQuestion;
    }

    public void setMandatoryQuestion(Boolean mandatoryQuestion) {
        this.mandatoryQuestion = mandatoryQuestion;
    }

    public Integer getMaximumCapacityOfAnswer() {
        return maximumCapacityOfAnswer;
    }

    public void setMaximumCapacityOfAnswer(Integer maximumCapacityOfAnswer) {
        this.maximumCapacityOfAnswer = maximumCapacityOfAnswer;
    }

}
