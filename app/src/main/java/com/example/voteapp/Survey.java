package com.example.voteapp;

import java.sql.Date;

public class Survey {
    private Long id;

    private String voteTitle;
    private Date createdDate;
    private Long author_id;
    private Date startDate;
    private Date endDate;
    private Boolean isPublicVote;
    private Boolean isAnonymousVote;
    private Boolean isMandatory;
    private String votePassword;

    public Survey(Long id, String voteTitle, Date createdDate, Long author_id, Date startDate, Date endDate, Boolean isPublicVote, Boolean isAnonymousVote, Boolean isMandatory, String votePassword) {
        this.id = id;
        this.voteTitle = voteTitle;
        this.createdDate = createdDate;
        this.author_id = author_id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublicVote = isPublicVote;
        this.isAnonymousVote = isAnonymousVote;
        this.isMandatory = isMandatory;
        this.votePassword = votePassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getVotePassword() {
        return votePassword;
    }

    public void setVotePassword(String votePassword) {
        this.votePassword = votePassword;
    }
}
