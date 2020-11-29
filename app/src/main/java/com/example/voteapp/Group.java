package com.example.voteapp;

import java.io.Serializable;

public class Group implements Serializable {

    private String id;
    private Boolean active;
    private String name;
    private String description;
    private Boolean is_public;
    private String group_password;
    private String picture_name;
    private Long owner_id;

    public Group(String id, Boolean active, String name, String description, Boolean is_public, String group_password, String picture_name, Long owner_id) {
        this.id = id;
        this.active = active;
        this.name = name;
        this.description = description;
        this.is_public = is_public;
        this.group_password = group_password;
        this.picture_name = picture_name;
        this.owner_id = owner_id;
    }

    public Group(String name, String description, Boolean is_public, String picture_name, Long owner_id) {
        this.name = name;
        this.description = description;
        this.is_public = is_public;
        this.picture_name = picture_name;
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

    public Boolean getIs_public() {
        return is_public;
    }

    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
    }

    public String getGroup_password() {
        return group_password;
    }

    public void setGroup_password(String group_password) {
        this.group_password = group_password;
    }

    public String getPicture_name() {
        return picture_name;
    }

    public void setPicture_name(String picture_name) {
        this.picture_name = picture_name;
    }

    public Long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Long owner_id) {
        this.owner_id = owner_id;
    }
}
