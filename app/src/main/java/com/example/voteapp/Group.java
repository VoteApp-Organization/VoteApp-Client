package com.example.voteapp;

import java.io.Serializable;

public class Group implements Serializable {

    private String id;
    private Boolean active;
    private String name;
    private String description;
    private Boolean isPublic;
    private String groupPassword;
    private String pictureName;
    private Long owner_id;

    public Group(String id, Boolean active, String name, String description, Boolean isPublic, String groupPassword, String pictureName, Long owner_id) {
        this.id = id;
        this.active = active;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.groupPassword = groupPassword;
        this.pictureName = pictureName;
        this.owner_id = owner_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public Long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Long owner_id) {
        this.owner_id = owner_id;
    }
}
