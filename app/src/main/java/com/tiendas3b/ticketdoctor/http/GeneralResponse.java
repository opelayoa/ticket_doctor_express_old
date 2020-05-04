package com.tiendas3b.ticketdoctor.http;

/**
 * Created by dfa on 15/03/2016.
 */
public class GeneralResponse {

    private long code;
    private String description;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
