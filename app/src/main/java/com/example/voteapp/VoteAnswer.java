package com.example.voteapp;

import java.io.Serializable;

public class VoteAnswer implements Serializable {

    private Long question_id;
    private Long vote_id;
    private String answerContent;

    public VoteAnswer(Long question_id, Long vote_id, String answerContent) {
        this.question_id = question_id;
        this.vote_id = vote_id;
        this.answerContent = answerContent;
    }

    public Long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public Long getVote_id() {
        return vote_id;
    }

    public void setVote_id(Long vote_id) {
        this.vote_id = vote_id;
    }

    public Long getVote_Id() {
        return vote_id;
    }

    public void setVote_Id(Long vote_Id) {
        this.vote_id = vote_Id;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }
}
