package com.tiendas3b.ticketdoctor.dto;

import com.tiendas3b.ticketdoctor.db.dao.Access;
import com.tiendas3b.ticketdoctor.db.dao.User;

import java.util.List;

/**
 * Created by dfa on 24/02/2016.
 */
public class UserDTO {

    private User user;
    private List<Access> accesses;

    public UserDTO() {
    }

    public UserDTO(User user, List<Access> accesses) {
        this.user = user;
        this.accesses = accesses;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Access> getAccesses() {
        return accesses;
    }

    public void setAccesses(List<Access> accesses) {
        this.accesses = accesses;
    }
}
