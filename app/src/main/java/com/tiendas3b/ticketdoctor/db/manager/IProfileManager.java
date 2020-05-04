package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.Profile;

import java.util.List;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IProfileManager extends IDatabaseManager {

    List<Profile> getProfileByUserId(Long id);

    void insertOrReplaceInTxProfiles(Profile... actions);
}
