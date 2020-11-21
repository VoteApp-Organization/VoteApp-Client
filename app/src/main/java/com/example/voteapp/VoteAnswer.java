package com.example.voteapp;

import java.io.Serializable;

public class VoteAnswer implements Serializable {

    private Long id;
    private Long vote_Id;
    private String answerContent;

    public VoteAnswer(Long id, Long vote_Id, String answerContent) {
        this.id = id;
        this.vote_Id = vote_Id;
        this.answerContent = answerContent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVote_Id() {
        return vote_Id;
    }

    public void setVote_Id(Long vote_Id) {
        this.vote_Id = vote_Id;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }
}
