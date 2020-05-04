package com.tiendas3b.ticketdoctor.db.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "ACCESS".
 */
public class Access {

    private Long id;
    private long profileId;
    /** Not-null value. */
    private String action;
    /** Not-null value. */
    private String node;

    public Access() {
    }

    public Access(Long id) {
        this.id = id;
    }

    public Access(Long id, long profileId, String action, String node) {
        this.id = id;
        this.profileId = profileId;
        this.action = action;
        this.node = node;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    /** Not-null value. */
    public String getAction() {
        return action;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAction(String action) {
        this.action = action;
    }

    /** Not-null value. */
    public String getNode() {
        return node;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNode(String node) {
        this.node = node;
    }

}
