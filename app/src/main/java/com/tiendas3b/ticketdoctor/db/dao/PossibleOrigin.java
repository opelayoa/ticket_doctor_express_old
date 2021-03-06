package com.tiendas3b.ticketdoctor.db.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "POSSIBLE_ORIGIN".
 */
public class PossibleOrigin extends EqualsBase  {

    private Long id;
    /** Not-null value. */
    private String name;
    private String description;

    public PossibleOrigin() {
    }

    public PossibleOrigin(Long id) {
        this.id = id;
    }

    public PossibleOrigin(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
